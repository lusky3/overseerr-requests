package app.lusk.client.presentation.request

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.lusk.client.domain.model.MediaType
import app.lusk.client.domain.repository.QualityProfile
import app.lusk.client.domain.repository.RootFolder

@Composable
fun RequestDialog(
    mediaId: Int,
    mediaType: MediaType,
    mediaTitle: String,
    viewModel: RequestViewModel,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val requestState by viewModel.requestState.collectAsState()
    val qualityProfiles by viewModel.qualityProfiles.collectAsState()
    val rootFolders by viewModel.rootFolders.collectAsState()
    
    var selectedQualityProfile by remember { mutableStateOf<Int?>(null) }
    var selectedRootFolder by remember { mutableStateOf<String?>(null) }
    var selectedSeasons by remember { mutableStateOf<List<Int>>(emptyList()) }
    var showAdvancedOptions by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.loadQualityProfiles()
        viewModel.loadRootFolders()
    }
    
    LaunchedEffect(requestState) {
        if (requestState is RequestState.Success) {
            onSuccess()
            viewModel.clearRequestState()
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Request $mediaTitle") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (mediaType == MediaType.TV) {
                    SeasonSelector(
                        selectedSeasons = selectedSeasons,
                        onSeasonsChanged = { selectedSeasons = it }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Advanced Options")
                    Switch(
                        checked = showAdvancedOptions,
                        onCheckedChange = { showAdvancedOptions = it }
                    )
                }
                
                if (showAdvancedOptions) {
                    if (qualityProfiles.isNotEmpty()) {
                        QualityProfileSelector(
                            profiles = qualityProfiles,
                            selectedProfile = selectedQualityProfile,
                            onProfileSelected = { selectedQualityProfile = it }
                        )
                    }
                    
                    if (rootFolders.isNotEmpty()) {
                        RootFolderSelector(
                            folders = rootFolders,
                            selectedFolder = selectedRootFolder,
                            onFolderSelected = { selectedRootFolder = it }
                        )
                    }
                }
                
                when (requestState) {
                    is RequestState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is RequestState.Error -> {
                        Text(
                            text = (requestState as RequestState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    else -> {}
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when (mediaType) {
                        MediaType.MOVIE -> {
                            viewModel.submitMovieRequest(
                                movieId = mediaId,
                                qualityProfile = selectedQualityProfile,
                                rootFolder = selectedRootFolder
                            )
                        }
                        MediaType.TV -> {
                            if (selectedSeasons.isNotEmpty()) {
                                viewModel.submitTvShowRequest(
                                    tvShowId = mediaId,
                                    seasons = selectedSeasons,
                                    qualityProfile = selectedQualityProfile,
                                    rootFolder = selectedRootFolder
                                )
                            }
                        }
                    }
                },
                enabled = requestState !is RequestState.Loading &&
                        (mediaType == MediaType.MOVIE || selectedSeasons.isNotEmpty())
            ) {
                Text("Request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
}

@Composable
private fun SeasonSelector(
    selectedSeasons: List<Int>,
    onSeasonsChanged: (List<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Select Seasons",
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onSeasonsChanged(listOf(0)) },
                modifier = Modifier.weight(1f)
            ) {
                Text("All Seasons")
            }
            
            Button(
                onClick = { onSeasonsChanged(emptyList()) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
        }
        
        if (selectedSeasons.isNotEmpty()) {
            Text(
                text = if (selectedSeasons.contains(0)) {
                    "All seasons selected"
                } else {
                    "Seasons: ${selectedSeasons.joinToString(", ")}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QualityProfileSelector(
    profiles: List<QualityProfile>,
    selectedProfile: Int?,
    onProfileSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Quality Profile",
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        profiles.forEach { profile ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedProfile == profile.id,
                        onClick = { onProfileSelected(profile.id) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedProfile == profile.id,
                    onClick = { onProfileSelected(profile.id) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(profile.name)
            }
        }
    }
}

@Composable
private fun RootFolderSelector(
    folders: List<RootFolder>,
    selectedFolder: String?,
    onFolderSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Root Folder",
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        folders.forEach { folder ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedFolder == folder.id,
                        onClick = { onFolderSelected(folder.id) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedFolder == folder.id,
                    onClick = { onFolderSelected(folder.id) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(folder.path, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
