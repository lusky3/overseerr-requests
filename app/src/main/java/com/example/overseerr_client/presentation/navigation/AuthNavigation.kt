package com.example.overseerr_client.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.overseerr_client.presentation.auth.AuthErrorScreen
import com.example.overseerr_client.presentation.auth.PlexAuthScreen
import com.example.overseerr_client.presentation.auth.ServerConfigScreen

/**
 * Navigation routes for authentication flow.
 */
sealed class AuthRoute(val route: String) {
    data object ServerConfig : AuthRoute("server_config")
    data object PlexAuth : AuthRoute("plex_auth")
    data object AuthError : AuthRoute("auth_error/{errorMessage}") {
        fun createRoute(errorMessage: String) = "auth_error/$errorMessage"
    }
}

/**
 * Authentication navigation graph.
 * Feature: overseerr-android-client
 * Validates: Requirements 1.1, 1.3, 1.7
 */
@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    onAuthComplete: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.ServerConfig.route
    ) {
        composable(AuthRoute.ServerConfig.route) {
            ServerConfigScreen(
                onServerValidated = {
                    navController.navigate(AuthRoute.PlexAuth.route)
                }
            )
        }
        
        composable(AuthRoute.PlexAuth.route) {
            PlexAuthScreen(
                onAuthSuccess = onAuthComplete,
                onAuthError = { errorMessage ->
                    navController.navigate(AuthRoute.AuthError.createRoute(errorMessage))
                }
            )
        }
        
        composable(AuthRoute.AuthError.route) { backStackEntry ->
            val errorMessage = backStackEntry.arguments?.getString("errorMessage") ?: "Unknown error"
            AuthErrorScreen(
                errorMessage = errorMessage,
                onRetry = {
                    navController.navigate(AuthRoute.PlexAuth.route) {
                        popUpTo(AuthRoute.AuthError.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigate(AuthRoute.ServerConfig.route) {
                        popUpTo(AuthRoute.AuthError.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
