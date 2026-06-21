package com.anishkun.hidetext.domain.crypto

/**
 * Interface representing the cryptographic operations required for End-to-End Encryption.
 */
interface CryptoManager {
    /**
     * Generates a new Public/Private key pair and stores the Private Key securely.
     * @return The base64 encoded Public Key to share with the backend.
     */
    fun generateKeys(): String

    /**
     * Retrieves the local base64 encoded Public Key if it exists.
     */
    fun getPublicKey(): String?

    /**
     * Encrypts plain text using the recipient's base64 encoded Public Key.
     */
    fun encrypt(plainText: String, recipientPublicKeyBase64: String): String

    /**
     * Decrypts cipher text using the local secure Private Key.
     */
    fun decrypt(cipherText: String): String
}
