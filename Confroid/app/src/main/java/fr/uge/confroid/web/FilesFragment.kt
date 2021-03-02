package fr.uge.confroid.web

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_files.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class FilesFragment : Fragment(R.layout.fragment_files), FileAdapter.OnFileListener {

    private val fileAdapter : FileAdapter = FileAdapter(this, listOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SharedPreferences.getInstance(activity!!).isLoggedIn()) {
            getAllFiles()
        }

        filesRecyclerView.adapter = fileAdapter
        filesRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        filesRecyclerView.setHasFixedSize(true)

    }

    override fun onClickListener(fileAttributes: FileAttributes) {
        val user = SharedPreferences.getInstance(activity!!).getUser()
        getFile(fileAttributes.name, user.password, user.token)
    }

    private fun getFile(name : String, password: String, token : String) {
        val customRequest : CustomRequest<ByteArray> = @RequiresApi(Build.VERSION_CODES.O)
        object : CustomRequest<ByteArray>(Method.GET, "${URL.ROOT_FILE}/$name", ByteArray::class.java,
            {
                try {
                    if (it != null) {
                        val secretKey : SecretKey = SecretKeySpec(CryptKey.decrypt(password.toByteArray(), CryptKey.secretKey), "AES")
                        val decrypted = CryptKey.decrypt(it, secretKey)

                        val outputStream = activity!!.openFileOutput(name, Context.MODE_PRIVATE)
                        outputStream.write(decrypted)
                        outputStream.close()
                        val file = File(activity!!.filesDir, name)
                        //CryptKey.decryptFile(file)
                        //Log.i("content", "after->${String(CryptKey.decrypt(it)!!)}")
                        Toast.makeText(activity, "Download $name completed", Toast.LENGTH_LONG).show()
                        Log.i("file", "content:${file.readText()}")
                    }
                } catch (e : Exception) {
                    Log.e("KEY_ERROR", "UNABLE TO DOWNLOAD FILE")
                    e.printStackTrace()
                }
            },
            {
                Log.e("error get", it.toString())
                SharedPreferences.getInstance(activity!!).logout()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<ByteArray> {
                return Response.success( response?.data, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        VolleySingleton.getInstance(activity!!).addToRequestQueue(customRequest)
    }

    private fun getAllFiles() {
        if (SharedPreferences.getInstance(activity!!).isLoggedIn()) {
            val user = SharedPreferences.getInstance(activity!!).getUser()
            LoginRequest.request(activity!!, user.username, user.password) {}

            Log.i("shared user", user.toString())
            getFiles(user.token)

        }

    }

    private fun parse(result: String) : List<FileAttributes> {
        val list = Gson().fromJson(result, List::class.java)
        return list.map {
            val jsonObject = it as Map<String, String>
            FileAttributes(jsonObject["name"] ?: error(""), jsonObject["url"] ?: error(""))
        }
    }

    private suspend fun updateAdapter(requestBody : String) {
        val files = parse(requestBody)
        fileAdapter.requests = files
        fileAdapter.notifyDataSetChanged()
    }

    private fun getFiles(token: String) {
        val filesRequest = object : StringRequest(Method.GET, URL.ROOT_FILES,
            {
                runBlocking {
                    updateAdapter(it)
                }
            },
            {
                Log.e("files error", it.toString())
                SharedPreferences.getInstance(activity!!).logout()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }
        }

        VolleySingleton.getInstance(activity!!).addToRequestQueue(filesRequest)
    }

}