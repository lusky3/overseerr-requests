package app.lusk.client.presentation.discovery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import app.lusk.client.domain.model.MediaType
import androidx.hilt.navigation.compose.hiltViewModel
import app.lusk.client.presentation.request.RequestDialog
import app.lusk.client.presentation.request.RequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailsScreen(
    viewModel: DiscoveryViewModel,
    mediaType: MediaType,
    mediaId: Int,
    openRequest: Boolean = false,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val requestViewModel: RequestViewModel = hiltViewModel()
    val state by viewModel.mediaDetailsState.collectAsState()
    var showRequestDialog by remember { mutableStateOf(false) }
    
    // Auto-open request dialog if requested
    LaunchedEffect(openRequest) {
        if (openRequest) {
            showRequestDialog = true
        }
    }
    
    LaunchedEffect(mediaId, mediaType) {
        viewModel.loadMediaDetails(mediaType, mediaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val detailsState = state) {
                is MediaDetailsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is MediaDetailsState.Success -> {
                    val canModifyRequest = !detailsState.details.isAvailable && 
                        (detailsState.details.isRequested || detailsState.details.isPartiallyAvailable) && 
                        detailsState.details.isPartialRequestsEnabled && 
                        detailsState.details.numberOfSeasons > 0

                    MediaDetailsContent(
                        details = detailsState.details,
                        onRequestClick = { showRequestDialog = true }
                    )
                    
                    if (showRequestDialog) {
                        RequestDialog(
                            mediaId = mediaId,
                            mediaType = mediaType,
                            mediaTitle = detailsState.details.title,
                            seasonCount = detailsState.details.numberOfSeasons,
                            partialRequestsEnabled = detailsState.details.isPartialRequestsEnabled,
                            isModify = canModifyRequest,
                            requestedSeasons = detailsState.details.requestedSeasons,
                            viewModel = requestViewModel,
                            onDismiss = { showRequestDialog = false },
                            onSuccess = {
                                showRequestDialog = false
                                viewModel.loadMediaDetails(mediaType, mediaId)
                            }
                        )
                    }
                }
                is MediaDetailsState.Error -> {
                    ErrorDisplay(
                        message = detailsState.message,
                        onRetry = { viewModel.loadMediaDetails(mediaType, mediaId) }
                    )
                }
                MediaDetailsState.Idle -> {
                    // Initial state, loading will be triggered by LaunchedEffect
                }
            }
        }
    }
}

@Composable
private fun MediaDetailsContent(
    details: MediaDetails,
    onRequestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Backdrop image
            details.backdropPath?.let { backdropPath ->
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w1280$backdropPath",
                    contentDescription = details.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Poster
                details.posterPath?.let { posterPath ->
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500$posterPath",
                        contentDescription = details.title,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Title and basic info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = details.title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    details.releaseDate?.let { releaseDate ->
                        Text(
                            text = releaseDate.take(4),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    details.voteAverage?.let { rating ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = String.format("%.1f", rating),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // Request button
                    val canModifyRequest = !details.isAvailable && 
                        (details.isRequested || details.isPartiallyAvailable) && 
                        details.isPartialRequestsEnabled && 
                        details.numberOfSeasons > 0 // Only for TV basically

                    Button(
                        onClick = onRequestClick,
                        enabled = (!details.isAvailable && !details.isRequested) || canModifyRequest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            when {
                                details.isAvailable -> "Available"
                                canModifyRequest -> "Modify Request"
                                details.isRequested -> "Requested"
                                else -> "Request"
                            }
                        )
                    }
                }
            }
        }

        item {
            // Overview
            details.overview?.let { overview ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = overview,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            // Genres
            if (details.genres.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Genres",
                        style = MaterialTheme.typography.titleLarge
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(details.genres) { genre ->
                            AssistChip(
                                onClick = { },
                                label = { Text(genre) }
                            )
                        }
                    }
                }
            }
        }

        item {
            // Runtime/Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                details.runtime?.let { runtime ->
                    InfoItem(
                        label = "Runtime",
                        value = "$runtime min"
                    )
                }

                details.status?.let { status ->
                    InfoItem(
                        label = "Status",
                        value = status
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ErrorDisplay(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
