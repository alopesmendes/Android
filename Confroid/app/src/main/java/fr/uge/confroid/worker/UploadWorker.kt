package fr.uge.confroid.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.web.*
import java.io.File
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class UploadWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if (!SharedPreferences.getInstance(applicationContext).isLoggedIn()) {
            return Result.failure()
        }
        val user = SharedPreferences.getInstance(applicationContext).getUser()
        LoginRequest.request(applicationContext, user.username, user.password) {}
        val files = MyProvider.getFiles(applicationContext)
        for (file in files) {
            sendFile(file, user.password, user.token)
        }
        return Result.success()
    }

    private fun sendFile(file : File, password : String,  token : String) {
        Log.i("preparing send","path:${file.absolutePath} and token:$token")

        val customRequest = object : VolleyFileUploadRequest(
            Method.POST, URL.ROOT_FILE_UPLOAD,
            {
                Log.i("success", it.toString())
            },
            {
                Log.e("fail", it.toString())
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getByteData(): Map<String, FileDataPart>? {
                val params = HashMap<String, FileDataPart>()
                val secretKey : SecretKey = SecretKeySpec(CryptKey.decrypt(password.toByteArray(), CryptKey.secretKey), "AES")
                params["file"] = FileDataPart(file.absolutePath, CryptKey.encrypt(file.readBytes(), secretKey)!!, "txt")
                return params
            }
        }

        VolleySingleton.getInstance(applicationContext).addToRequestQueue(customRequest)
    }
}