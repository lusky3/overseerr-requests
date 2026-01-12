package app.lusk.client.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.hilt.navigation.compose.hiltViewModel
import app.lusk.client.domain.model.MediaType
import app.lusk.client.presentation.auth.PlexAuthScreen
import app.lusk.client.presentation.auth.ServerConfigScreen
import app.lusk.client.presentation.discovery.DiscoveryViewModel
import app.lusk.client.presentation.discovery.HomeScreen
import app.lusk.client.presentation.discovery.MediaDetailsScreen
import app.lusk.client.presentation.discovery.SearchScreen
import app.lusk.client.presentation.profile.ProfileScreen
import app.lusk.client.presentation.request.RequestDetailsScreen
import app.lusk.client.presentation.request.RequestsListScreen
import app.lusk.client.presentation.settings.ServerManagementScreen
import app.lusk.client.presentation.settings.SettingsScreen
import app.lusk.client.ui.animation.backwardTransition
import app.lusk.client.ui.animation.forwardTransition
import app.lusk.client.ui.animation.popEnterTransition
import app.lusk.client.ui.animation.popExitTransition

/**
 * Navigation graph for the Overseerr Android Client.
 * Feature: overseerr-android-client
 * Validates: Requirements 2.4, 6.4
 */

/**
 * Main navigation host for the app.
 */
@Composable
fun OverseerrNavHost(
    navController: NavHostController,
    startDestination: String = Screen.ServerConfig.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Authentication Flow
        composable(
            route = Screen.ServerConfig.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            ServerConfigScreen(
                onServerValidated = {
                    navController.navigate(Screen.PlexAuth.route)
                },
                onAuthenticated = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.ServerConfig.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = Screen.PlexAuth.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            PlexAuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.ServerConfig.route) { inclusive = true }
                    }
                },
                onAuthError = { error ->
                    // Handle auth error - could navigate to error screen or show snackbar
                    navController.popBackStack()
                }
            )
        }
        
        // Main App Flow
        composable(
            route = Screen.Home.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            val viewModel: DiscoveryViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MediaDetails.createRoute("movie", movieId))
                },
                onTvShowClick = { tvShowId ->
                    navController.navigate(Screen.MediaDetails.createRoute("tv", tvShowId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }
        
        composable(
            route = Screen.Search.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            val viewModel: DiscoveryViewModel = hiltViewModel()
            SearchScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onMediaClick = { mediaType, mediaId ->
                    navController.navigate(Screen.MediaDetails.createRoute(mediaType.name.lowercase(), mediaId))
                }
            )
        }
        
        composable(
            route = Screen.MediaDetails.route,
            arguments = listOf(
                navArgument("mediaType") { type = NavType.StringType },
                navArgument("mediaId") { type = NavType.IntType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "lusk://media/{mediaType}/{mediaId}" }
            ),
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) { backStackEntry ->
            val mediaTypeString = backStackEntry.arguments?.getString("mediaType") ?: "movie"
            val mediaId = backStackEntry.arguments?.getInt("mediaId") ?: 0
            val mediaType = if (mediaTypeString == "tv") MediaType.TV else MediaType.MOVIE
            val viewModel: DiscoveryViewModel = hiltViewModel()
            
            MediaDetailsScreen(
                viewModel = viewModel,
                mediaType = mediaType,
                mediaId = mediaId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Requests Flow
        composable(
            route = Screen.Requests.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            val viewModel: app.lusk.client.presentation.request.RequestViewModel = hiltViewModel()
            RequestsListScreen(
                viewModel = viewModel,
                onRequestClick = { requestId ->
                    navController.navigate(Screen.RequestDetails.createRoute(requestId))
                }
            )
        }
        
        composable(
            route = Screen.RequestDetails.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.IntType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "lusk://request/{requestId}" }
            ),
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getInt("requestId") ?: 0
            val viewModel: app.lusk.client.presentation.request.RequestViewModel = hiltViewModel()
            
            RequestDetailsScreen(
                requestId = requestId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Profile Flow
        composable(
            route = Screen.Profile.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            ProfileScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    navController.navigate(Screen.ServerConfig.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = Screen.Settings.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            SettingsScreen(
                onNavigateToServerManagement = {
                    navController.navigate(Screen.ServerManagement.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.ServerManagement.route,
            enterTransition = { forwardTransition() },
            exitTransition = { backwardTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            ServerManagementScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
