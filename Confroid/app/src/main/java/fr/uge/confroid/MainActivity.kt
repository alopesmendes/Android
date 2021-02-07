package fr.uge.confroid

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.web.*
import fr.uge.confroid.worker.UploadWorker
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity(), RequestAdapter.OnUrlListener {
    private val requestAdapter = RequestAdapter(this, mutableListOf())

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);
        // The differents files created
        MyProvider.writeFile(applicationContext, "mmtext.txt", "On est la".toByteArray())
        MyProvider.writeFile(applicationContext, "save.txt", "Sauvegarde les secrets".toByteArray())
        MyProvider.writeFile(applicationContext, "allo.txt", "Allo les gens".toByteArray())
        MyProvider.writeFile(applicationContext, "bonjour.txt", "Bonjour les amis".toByteArray())
        MyProvider.writeFile(applicationContext, "anime.txt", "Cest bien les animes".toByteArray())
        MyProvider.writeFile(applicationContext, "cours.txt", "Pas ouf".toByteArray())
        MyProvider.writeFile(applicationContext, "eau.txt", "H20".toByteArray())
        MyProvider.writeFile(applicationContext, "avatar.txt", "Les 4 elements".toByteArray())

        if (SharedPreferences.getInstance(this).isLoggedIn()) {
            val user = SharedPreferences.getInstance(this).getUser()
            LoginRequest.request(this, user.username, user.password) {}
            buttonGetFile.visibility = View.VISIBLE

            work()

            buttonGetFile.setOnClickListener {
                val user = SharedPreferences.getInstance(this).getUser()
                Log.i("shared user", user.toString())
                //getFile("mmtext.txt", user.password, user.token)
                getFiles(user.token)
            }

        } else {
            buttonGetFile.visibility = View.INVISIBLE
        }

        requestRecyclerView.adapter = requestAdapter
        requestRecyclerView.layoutManager = LinearLayoutManager(this)
        requestRecyclerView.setHasFixedSize(true)

    }

    private fun work() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val work = PeriodicWorkRequestBuilder<UploadWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(work)
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

    private fun parse(result: String) : List<FileAttributes> {
        val list = Gson().fromJson(result, List::class.java)
        return list.map {
            val jsonObject = it as Map<String, String>
            FileAttributes(jsonObject["name"] ?: error(""), jsonObject["url"] ?: error(""))
        }
    }

    private fun getFiles(token: String) {
        val filesRequest = object : StringRequest(Method.GET, URL.ROOT_FILES,
            {
                requestAdapter.fas = parse(it)
                requestAdapter.notifyDataSetChanged()
            },
            {
                Log.e("files error", it.toString())
                SharedPreferences.getInstance(this).logout()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(filesRequest)
    }

    private fun getFile(name : String, password: String, token : String) {
        val customRequest : CustomRequest<ByteArray> = @RequiresApi(Build.VERSION_CODES.O)
        object : CustomRequest<ByteArray>(Method.GET, "${URL.ROOT_FILE}/$name", ByteArray::class.java,
            {
                try {
                    if (it != null) {
                        val secretKey : SecretKey = SecretKeySpec(CryptKey.decrypt(password.toByteArray(), CryptKey.secretKey), "AES")
                        val decrypted = CryptKey.decrypt(it, secretKey)

                        val outputStream = openFileOutput(name, Context.MODE_PRIVATE)
                        outputStream.write(decrypted)
                        outputStream.close()
                        val file = File(filesDir, name)
                        //CryptKey.decryptFile(file)
                        //Log.i("content", "after->${String(CryptKey.decrypt(it)!!)}")
                        Toast.makeText(this, "Download completed", Toast.LENGTH_LONG).show()
                        Log.i("file", "content:${file.readText()}")
                    }
                } catch (e : Exception) {
                    Log.e("KEY_ERROR", "UNABLE TO DOWNLOAD FILE")
                    e.printStackTrace()
                }
            },
            {
                Log.e("error get", it.toString())
                SharedPreferences.getInstance(this).logout()
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

    override fun onClickListener(fileAttributes: FileAttributes) {
        val user = SharedPreferences.getInstance(this).getUser()
        LoginRequest.request(this, user.username, user.password) {}
        getFile(fileAttributes.name, user.password, user.token)
    }
}