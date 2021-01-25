package fr.uge.confroid.web

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
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

    private val secretKey : SecretKey

    private val  IV : ByteArray = ByteArray(16)

    init {
        val keyGenerator : KeyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        secretKey = keyGenerator.generateKey()
        val random : SecureRandom = SecureRandom()
        random.nextBytes(IV)
    }
    /**
     * Will encrypt an text using AES and returns a encrypted ByteArray?
     *
     * @param plaintext the following ByteArray of an text
     * @return a ByteArray?
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun encrypt(plaintext: ByteArray?): ByteArray? {
        val cipher: Cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(secretKey.encoded, "AES")
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(plaintext)
    }

    /**
     * Will encrypt a text as many times the passed number and return the encrypted ByteArray?
     *
     * @param number the number of times plaintext should be encrypted
     * @param plaintext the following ByteArray of an text
     * @return a ByteArray?
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encrypt(plaintext: ByteArray?, number : Int): ByteArray? {
        return if (number <= 0) {
            plaintext
        } else {
            encrypt(encrypt(plaintext), number - 1)
        }
    }

    /**
     * Will decrypt a cipherText using AES and return the String?
     *
     * @param cipherText the following encrypted ByteArray?
     * @return a String?
     */
    fun decrypt(cipherText: ByteArray?): String? {
        try {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(secretKey.encoded, "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decryptedText = cipher.doFinal(cipherText)
            return String(decryptedText)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

}