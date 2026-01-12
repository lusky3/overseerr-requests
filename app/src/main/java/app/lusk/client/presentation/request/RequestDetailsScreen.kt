package app.lusk.client.presentation.request

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import app.lusk.client.domain.model.MediaRequest
import app.lusk.client.domain.model.RequestStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailsScreen(
    requestId: Int,
    viewModel: RequestViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userRequests by viewModel.userRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val request = userRequests.find { it.id == requestId }
    var showCancelDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (request?.status == RequestStatus.PENDING) {
                        IconButton(onClick = { showCancelDialog = true }) {
                            Icon(Icons.Default.Delete, "Cancel Request")
                        }
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
            when {
                request == null && !isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Request not found")
                    }
                }
                request != null -> {
                    RequestDetailsContent(request = request)
                }
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            
            if (error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(error!!)
                }
            }
        }
    }
    
    if (showCancelDialog && request != null) {
        CancelRequestDialog(
            requestTitle = request.title,
            onConfirm = {
                viewModel.cancelRequest(requestId)
                showCancelDialog = false
                onBackClick()
            },
            onDismiss = { showCancelDialog = false }
        )
    }
}

@Composable
private fun RequestDetailsContent(
    request: MediaRequest,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = request.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
                    contentDescription = request.title,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = request.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    
                    StatusChip(status = request.status)
                    
                    Text(
                        text = request.mediaType.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Request Information",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    InfoRow(
                        label = "Request ID",
                        value = request.id.toString()
                    )
                    
                    InfoRow(
                        label = "Status",
                        value = request.status.name
                    )
                    
                    InfoRow(
                        label = "Requested Date",
                        value = formatDate(request.requestedDate)
                    )
                    
                    InfoRow(
                        label = "Media Type",
                        value = request.mediaType.name
                    )
                    
                    if (request.seasons != null && request.seasons.isNotEmpty()) {
                        InfoRow(
                            label = "Seasons",
                            value = if (request.seasons.contains(0)) {
                                "All seasons"
                            } else {
                                request.seasons.joinToString(", ")
                            }
                        )
                    }
                }
            }
        }
        
        item {
            when (request.status) {
                RequestStatus.PENDING -> {
                    InfoCard(
                        title = "Pending Approval",
                        message = "Your request is waiting for approval from an administrator."
                    )
                }
                RequestStatus.APPROVED -> {
                    InfoCard(
                        title = "Approved",
                        message = "Your request has been approved and is being processed."
                    )
                }
                RequestStatus.AVAILABLE -> {
                    InfoCard(
                        title = "Available",
                        message = "The requested media is now available in your Plex library!"
                    )
                }
                RequestStatus.DECLINED -> {
                    InfoCard(
                        title = "Declined",
                        message = "Your request was declined by an administrator."
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun StatusChip(
    status: RequestStatus,
    modifier: Modifier = Modifier
) {
    val (color, text) = when (status) {
        RequestStatus.PENDING -> MaterialTheme.colorScheme.tertiary to "Pending"
        RequestStatus.APPROVED -> MaterialTheme.colorScheme.primary to "Approved"
        RequestStatus.AVAILABLE -> MaterialTheme.colorScheme.secondary to "Available"
        RequestStatus.DECLINED -> MaterialTheme.colorScheme.error to "Declined"
    }
    
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun CancelRequestDialog(
    requestTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancel Request") },
        text = { Text("Are you sure you want to cancel the request for \"$requestTitle\"?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cancel Request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Keep Request")
            }
        }
    )
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
