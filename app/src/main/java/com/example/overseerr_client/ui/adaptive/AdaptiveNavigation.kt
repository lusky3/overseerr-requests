package com.example.overseerr_client.ui.adaptive

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Adaptive navigation components that adjust based on screen size.
 * Feature: overseerr-android-client
 * Validates: Requirements 9.4
 * Property 35: Adaptive Layout Responsiveness
 */

/**
 * Navigation destination data class.
 */
data class NavigationDestination(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * Default navigation destinations.
 */
val defaultNavigationDestinations = listOf(
    NavigationDestination(
        route = "home",
        icon = Icons.Default.Home,
        label = "Home"
    ),
    NavigationDestination(
        route = "requests",
        icon = Icons.Default.PlayArrow,
        label = "Requests"
    ),
    NavigationDestination(
        route = "profile",
        icon = Icons.Default.Person,
        label = "Profile"
    )
)

/**
 * Adaptive navigation that switches between bottom bar and navigation rail.
 */
@Composable
fun AdaptiveNavigation(
    currentRoute: String,
    destinations: List<NavigationDestination> = defaultNavigationDestinations,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutConfig = rememberAdaptiveLayoutConfig()
    
    if (layoutConfig.useNavigationRail) {
        NavigationRail(modifier = modifier) {
            Spacer(modifier = Modifier.height(16.dp))
            destinations.forEach { destination ->
                NavigationRailItem(
                    selected = currentRoute == destination.route,
                    onClick = { onNavigate(destination.route) },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = if (layoutConfig.showNavigationLabels) {
                        { Text(destination.label) }
                    } else null
                )
            }
        }
    } else {
        NavigationBar(modifier = modifier) {
            destinations.forEach { destination ->
                NavigationBarItem(
                    selected = currentRoute == destination.route,
                    onClick = { onNavigate(destination.route) },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = if (layoutConfig.showNavigationLabels) {
                        { Text(destination.label) }
                    } else null
                )
            }
        }
    }
}

/**
 * Bottom navigation bar for compact screens.
 */
@Composable
fun CompactNavigation(
    currentRoute: String,
    destinations: List<NavigationDestination> = defaultNavigationDestinations,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = { onNavigate(destination.route) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}

/**
 * Navigation rail for medium and expanded screens.
 */
@Composable
fun ExpandedNavigation(
    currentRoute: String,
    destinations: List<NavigationDestination> = defaultNavigationDestinations,
    onNavigate: (String) -> Unit,
    showLabels: Boolean = true,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        destinations.forEach { destination ->
            NavigationRailItem(
                selected = currentRoute == destination.route,
                onClick = { onNavigate(destination.route) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = if (showLabels) {
                    { Text(destination.label) }
                } else null
            )
        }
    }
}
