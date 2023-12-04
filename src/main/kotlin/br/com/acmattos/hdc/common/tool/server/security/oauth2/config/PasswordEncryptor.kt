package br.com.acmattos.hdc.common.tool.server.security.oauth2.config

import org.apache.commons.codec.binary.Hex
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * @author ACMattos
 * @since 28/03/2023.
 */
class PasswordEncryptor(
    var salt: String? = null,
    private val secret: String,
    private val password: String,
) {
    var encrypted: EncryptedPassword
    init {
        salt = salt ?: generateRandomSalt().toHexString()
        encrypted = generateHash()
    }

    private fun generateRandomSalt(): ByteArray {
        val salt = ByteArray(64)
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun generateHash(): EncryptedPassword {
        val combinedSalt = "$salt$secret".toByteArray()
        val key: SecretKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(
            PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        )
        return EncryptedPassword(key.encoded.toHexString())
    }

    companion object {
        private const val ALGORITHM = "PBKDF2WithHmacSHA512"
        private const val ITERATIONS = 240_000
        private const val KEY_LENGTH = 1024
    }
}

/**
 * @author ACMattos
 * @since 22/11/2023.
 */
data class EncryptedPassword(val value: String)

private fun ByteArray.toHexString(): String =
    Hex.encodeHexString(this)