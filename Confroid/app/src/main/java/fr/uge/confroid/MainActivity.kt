package fr.uge.confroid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.web.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);


        if (SharedPreferences.getInstance(this).isLoggedIn()) {
            val user = SharedPreferences.getInstance(this).getUser()
            Log.i("shared user", user.toString())
            buttonSendFile.visibility = View.VISIBLE
            buttonGetFile.visibility = View.VISIBLE
            val myProvider = MyProvider()

            buttonSendFile.setOnClickListener {
                sendFile(myProvider.getFile(this), user.token)
            }

            buttonGetFile.setOnClickListener {
                getFile("mmtext.txt", user.token)
            }

        } else {
            buttonSendFile.visibility = View.INVISIBLE
            buttonGetFile.visibility = View.INVISIBLE
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

    private fun getFile(name : String, token : String) {
        Log.i("preparing get", "file:$name and token:$token")
        val customRequest : CustomRequest<ByteArray> = object : CustomRequest<ByteArray>(Method.GET, "${URL.ROOT_FILE}/$name", ByteArray::class.java,
            {
                try {
                    if (it != null) {
                        val outputStream = openFileOutput(name, Context.MODE_PRIVATE)
                        outputStream.write(it)
                        outputStream.close()
                        Toast.makeText(this, "Download completed", Toast.LENGTH_LONG).show()
                        Log.i("info files", this.filesDir.list().joinToString(", ", "{", "}"))
                    }
                } catch (e : Exception) {
                    Log.e("KEY_ERROR", "UNABLE TO DOWNLOAD FILE")
                    e.printStackTrace()
                }
            },
            {
                Log.e("error get", it.toString())
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<ByteArray> {
                return Response.success( response?.data, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(customRequest)
    }
}