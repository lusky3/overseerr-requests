package com.example.overseerr_client.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Plex authentication screen with OAuth flow.
 * Feature: overseerr-android-client
 * Validates: Requirements 1.1, 1.3, 1.4
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlexAuthScreen(
    onAuthSuccess: () -> Unit,
    onAuthError: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    
    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Authenticated -> onAuthSuccess()
            is AuthState.Error -> onAuthError(state.message)
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Authenticate with Plex") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (authState) {
                is AuthState.AuthenticatingWithPlex -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Opening Plex authentication...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Please complete authentication in your browser",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                is AuthState.ExchangingToken -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Exchanging token...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Connecting to Overseerr server",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                else -> {
                    Text(
                        text = "Sign in with Plex",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "Use your Plex account to access Overseerr",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    
                    Button(
                        onClick = { viewModel.initiateAuth() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign in with Plex")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "You'll be redirected to Plex to authorize this app",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
