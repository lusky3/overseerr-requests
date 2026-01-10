package com.example.overseerr_client.domain.security

import java.security.cert.X509Certificate

/**
 * Interface for security operations including encryption, secure storage,
 * and certificate validation.
 */
interface SecurityManager {
    /**
     * Encrypts data using Android Keystore.
     */
    suspend fun encryptData(data: String): String

    /**
     * Decrypts data using Android Keystore.
     */
    suspend fun decryptData(encryptedData: String): String

    /**
     * Stores data securely using EncryptedSharedPreferences.
     */
    suspend fun storeSecureData(key: String, value: String)

    /**
     * Retrieves securely stored data.
     */
    suspend fun retrieveSecureData(key: String): String?

    /**
     * Clears all securely stored data.
     */
    suspend fun clearSecureData()

    /**
     * Validates SSL certificate for the given hostname.
     */
    fun validateCertificate(hostname: String, certificate: X509Certificate): Boolean
}
