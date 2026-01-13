package app.lusk.client.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.lusk.client.data.local.dao.MediaRequestDao
import app.lusk.client.data.local.dao.OfflineRequestDao
import app.lusk.client.data.remote.api.RequestApiService
import app.lusk.client.data.remote.model.toMediaRequest
import app.lusk.client.data.mapper.toEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class OfflineRequestWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val offlineRequestDao: OfflineRequestDao,
    private val requestApiService: RequestApiService,
    private val mediaRequestDao: MediaRequestDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (runAttemptCount > 3) return Result.failure()

        val offlineRequests = offlineRequestDao.getAll()
        if (offlineRequests.isEmpty()) return Result.success()

        var allSuccess = true

        offlineRequests.forEach { request ->
            try {
                val response = if (request.mediaType == "movie") {
                    requestApiService.requestMovie(
                        movieId = request.mediaId, 
                        qualityProfile = request.qualityProfile, 
                        rootFolder = request.rootFolder
                    )
                } else {
                    val seasons = request.seasons?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
                    requestApiService.requestTvShow(
                        tvShowId = request.mediaId, 
                        seasons = seasons,
                        qualityProfile = request.qualityProfile, 
                        rootFolder = request.rootFolder
                    )
                }
                 
                // Success - Save true request to DB
                val mediaRequest = response.toMediaRequest()
                mediaRequestDao.insert(mediaRequest.toEntity())
                 
                // Remove offline request
                offlineRequestDao.delete(request)
                 
            } catch (e: Exception) {
                if (e is HttpException && e.code() in 400..499) {
                    // Fatal error (e.g. already requested, 403, etc), don't retry, just delete
                    offlineRequestDao.delete(request)
                } else {
                    // Start next cycle effectively
                    allSuccess = false
                }
            }
        }
        
        return if (allSuccess) Result.success() else Result.retry()
    }
}
