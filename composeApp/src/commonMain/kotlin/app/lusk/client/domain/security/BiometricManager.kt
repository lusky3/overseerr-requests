package app.lusk.client.domain.security

/**
 * Interface for checking biometric capabilities.
 * Feature: overseerr-android-client
 * Validates: Requirements 8.3
 */
interface BiometricManager {
    /**
     * Check if biometric authentication is available on this device.
     */
    fun isBiometricAvailable(): Boolean
}
