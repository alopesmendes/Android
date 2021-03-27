package fr.uge.confroid.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.NetworkResponse
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.web.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class UploadWorker(val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        if (!WebSharedPreferences.getInstance(applicationContext).isLoggedIn()) {
            return Result.failure()
        }
        val user = WebSharedPreferences.getInstance(applicationContext).getUser()
        LoginRequest.request(applicationContext, user.username, user.password) {}
        val files = MyProvider.getFiles(applicationContext)
        for (file in files) {
            sendFile(file, user.password, user.token)
        }
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendFile(file: File, password: String, token: String) {
        Log.i("preparing send", "path:${file.absolutePath} and token:$token")

        val secretKey: SecretKey = SecretKeySpec(
            CryptKey.decrypt(password.toByteArray(), CryptKey.secretKey),
            "AES"
        )
        val data = FormatDataFileRequest(
            file.absolutePath,
            CryptKey.encrypt(file.readBytes(), secretKey)!!
        )


        val cs = object :
            CustomRequest<NetworkResponse>(Method.POST,
                URL.ROOT_FILE_UPLOAD,
                NetworkResponse::class.java,
                {
                    Log.i("success", it.toString())
                },
                {
                    Log.e("fail", it.toString())
                    WebSharedPreferences.getInstance(appContext).logout()
                }) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }

            override fun getBodyContentType() = FormatDataFileRequest.bodyContentType()

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getBody(): ByteArray {
                val byteArrayOutputStream = ByteArrayOutputStream()
                val dataOutputStream = DataOutputStream(byteArrayOutputStream)
                try {

                    FormatDataFileRequest.processData(dataOutputStream, data)
                    return byteArrayOutputStream.toByteArray()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return super.getBody()
            }

        }

        VolleySingleton.getInstance(applicationContext).addToRequestQueue(cs)
    }
}