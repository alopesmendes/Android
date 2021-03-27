package fr.uge.confroid.web

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

/**
 * Will encrypt and decrypt using AES 256
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
object CryptKey {

    val secretKey : SecretKey

    init {
        secretKey = SecretKeySpec("&F)J@McQfTjWnZr4u7x!A%D*G-KaPdRg".toByteArray(), "AES")
    }

    /**
     * Will encrypt an text using AES and returns a encrypted ByteArray?
     *
     * @param plaintext the following ByteArray of an text
     * @param secretKey the secretKey from a encrypted(password) or CryptKey.secretKey
     * @return a ByteArray?
     * @throws Exception
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(Exception::class)
    fun encrypt(plaintext: ByteArray?, secretKey: SecretKey) : ByteArray? {
        val cipher: Cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(secretKey.encoded, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val cypherByteArray =  cipher.doFinal(plaintext)
        return Base64.getEncoder().encode(cypherByteArray)
    }

    /**
     * Will encrypt a text as many times the passed number and return the encrypted ByteArray?
     *
     * @param number the number of times plaintext should be encrypted
     * @param plaintext the following ByteArray of an text
     * @param secretKey the secretKey from a encrypted(password) or CryptKey.secretKey
     * @return a ByteArray?
     * @throws Exception
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(Exception::class)
    fun encrypt(plaintext: ByteArray?, secretKey: SecretKey, number: Int): ByteArray? {
        return if (number <= 0) {
            plaintext
        } else {
            encrypt(encrypt(plaintext, secretKey), secretKey, number - 1)
        }
    }

    /**
     * Will decrypt a cipherText using AES and return the String?
     *
     * @param cipherText the following encrypted ByteArray?
     * @param secretKey the secretKey from a encrypted(password) or CryptKey.secretKey
     * @return a String?
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(cipherText: ByteArray?, secretKey: SecretKey): ByteArray? {
        try {
            val base64data = Base64.getDecoder().decode(cipherText)
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(secretKey.encoded, "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            return cipher.doFinal(base64data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}