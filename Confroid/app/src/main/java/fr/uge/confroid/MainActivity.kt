package fr.uge.confroid

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.web.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);

        // Display's the username and the encrypted password if log
        // Bug : Does not work if we close the application.
        if (SharedPreferences.getInstance(this).isLoggedIn()) {
            val user = SharedPreferences.getInstance(this).getUser()
            usernameTextMain.visibility = View.VISIBLE
            passwordTextMain.visibility = View.VISIBLE
            usernameTextMain.text = user.username
            passwordTextMain.text = user.password

            val myProvider = MyProvider()
            //myProvider.shareFile(this)
            sendFile(myProvider.getFile(this), URL.token)

        } else {
            usernameTextMain.visibility = View.INVISIBLE
            passwordTextMain.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.register_login, menu)
        menu?.findItem(R.id.logoutItem)?.isVisible = SharedPreferences.getInstance(this).isLoggedIn()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.loginItem -> {
                Intent(this, LoginActivity::class.java).apply { startActivity(this) }
                true
            }
            R.id.logoutItem -> {
                SharedPreferences.getInstance(this).logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendFile(file : File, token : String) {
        Log.i("preparing send","path:${file.absolutePath} and token:$token")

        val customRequest = object : VolleyFileUploadRequest(Method.POST, URL.ROOT_FILE_UPLOAD,
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

            override fun getByteData(): Map<String, Any>? {
                val params = HashMap<String, FileDataPart>()
                params["file"] = FileDataPart(file.absolutePath, file.readBytes(), "txt")
                return params
            }
        }


        /*
        val customRequest = object : CustomRequest<NetworkResponse>(
            Method.POST, URL.ROOT_FILE_UPLOAD, NetworkResponse::class.java,
            {
                Log.i("success", it.toString())
            },
            {
                Log.e("error", it.toString())
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<NetworkResponse> {
                return try {
                    Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: Exception) {
                    Response.error(ParseError(e))
                }
            }

            override fun getBodyContentType(): String {
                return "multipart/form-data;"
            }

            override fun getBody(): ByteArray {
                val byteArrayOutputStream = ByteArrayOutputStream()
                val dataOutputStream = DataOutputStream(byteArrayOutputStream)
                val dataPart : MutableMap<String, FileDataPart> = HashMap()
                dataPart["file"] =  FileDataPart(file.absolutePath, file.readBytes(), "txt")
                dataPart.forEach {
                    val dataFile = it.value
                    val fileInputStream = ByteArrayInputStream(dataFile.data)
                    var bytesAvailable = fileInputStream.available()
                    val maxBufferSize = 1024 * 1024
                    var bufferSize = min(maxBufferSize, bytesAvailable)
                    val buffer = ByteArray(bufferSize)
                    var bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    while (bytesRead > 0) {
                        dataOutputStream.write(buffer, 0, bufferSize)
                        bytesAvailable = fileInputStream.available()
                        bufferSize = min(bytesAvailable, maxBufferSize)
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    }
                }
                return byteArrayOutputStream.toByteArray()
            }
        }

        */

        VolleySingleton.getInstance(this).addToRequestQueue(customRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            10 -> {
                if (requestCode == RESULT_OK) {
                    val path = data?.data?.path
                    Log.i("file select", "path : $path")
                }
                return
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}