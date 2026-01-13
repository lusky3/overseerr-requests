package app.lusk.client.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Window size class utilities for adaptive layouts.
 * Feature: overseerr-android-client
 * Validates: Requirements 9.4
 * Property 35: Adaptive Layout Responsiveness
 */

/**
 * Window size class based on Material Design 3 guidelines.
 */
enum class WindowSizeClass {
    COMPACT,    // Phone in portrait, width < 600dp
    MEDIUM,     // Tablet in portrait, foldable, 600dp <= width < 840dp
    EXPANDED    // Tablet in landscape, desktop, width >= 840dp
}

/**
 * Window height class for vertical responsiveness.
 */
enum class WindowHeightClass {
    COMPACT,    // height < 480dp
    MEDIUM,     // 480dp <= height < 900dp
    EXPANDED    // height >= 900dp
}

/**
 * Device posture for foldable devices.
 */
enum class DevicePosture {
    NORMAL,
    HALF_OPENED,
    FLAT,
    TENT
}

/**
 * Calculate window size class based on current window width.
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    return remember(screenWidth) {
        when {
            screenWidth < 600.dp -> WindowSizeClass.COMPACT
            screenWidth < 840.dp -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }
    }
}

/**
 * Calculate window height class based on current window height.
 */
@Composable
fun rememberWindowHeightClass(): WindowHeightClass {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    
    return remember(screenHeight) {
        when {
            screenHeight < 480.dp -> WindowHeightClass.COMPACT
            screenHeight < 900.dp -> WindowHeightClass.MEDIUM
            else -> WindowHeightClass.EXPANDED
        }
    }
}

/**
 * Adaptive layout configuration based on window size.
 */
data class AdaptiveLayoutConfig(
    val windowSizeClass: WindowSizeClass,
    val windowHeightClass: WindowHeightClass,
    val columns: Int,
    val contentPadding: Dp,
    val itemSpacing: Dp,
    val useNavigationRail: Boolean,
    val showNavigationLabels: Boolean
)

/**
 * Get adaptive layout configuration for current window size.
 */
@Composable
fun rememberAdaptiveLayoutConfig(): AdaptiveLayoutConfig {
    val windowSizeClass = rememberWindowSizeClass()
    val windowHeightClass = rememberWindowHeightClass()
    
    return remember(windowSizeClass, windowHeightClass) {
        when (windowSizeClass) {
            WindowSizeClass.COMPACT -> AdaptiveLayoutConfig(
                windowSizeClass = windowSizeClass,
                windowHeightClass = windowHeightClass,
                columns = 2,
                contentPadding = 16.dp,
                itemSpacing = 8.dp,
                useNavigationRail = false,
                showNavigationLabels = true
            )
            WindowSizeClass.MEDIUM -> AdaptiveLayoutConfig(
                windowSizeClass = windowSizeClass,
                windowHeightClass = windowHeightClass,
                columns = 3,
                contentPadding = 24.dp,
                itemSpacing = 12.dp,
                useNavigationRail = true,
                showNavigationLabels = true
            )
            WindowSizeClass.EXPANDED -> AdaptiveLayoutConfig(
                windowSizeClass = windowSizeClass,
                windowHeightClass = windowHeightClass,
                columns = 4,
                contentPadding = 32.dp,
                itemSpacing = 16.dp,
                useNavigationRail = true,
                showNavigationLabels = false
            )
        }
    }
}

/**
 * Extension function to check if device is in tablet mode.
 */
fun WindowSizeClass.isTablet(): Boolean {
    return this == WindowSizeClass.MEDIUM || this == WindowSizeClass.EXPANDED
}

/**
 * Extension function to check if device is in phone mode.
 */
fun WindowSizeClass.isPhone(): Boolean {
    return this == WindowSizeClass.COMPACT
}

/**
 * Extension function to get recommended grid columns.
 */
fun WindowSizeClass.getGridColumns(): Int {
    return when (this) {
        WindowSizeClass.COMPACT -> 2
        WindowSizeClass.MEDIUM -> 3
        WindowSizeClass.EXPANDED -> 4
    }
}
