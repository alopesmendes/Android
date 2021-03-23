package fr.uge.confroid.web

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import fr.uge.confroid.R
import fr.uge.confroid.utils.FilterUtils
import kotlinx.android.synthetic.main.fragment_files.*
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class FilesFragment : Fragment(R.layout.fragment_files), FileAdapter.OnFileListener {

    private val fileAdapter : FileAdapter = FileAdapter(this, listOf())

    private lateinit var navController: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        navController = Navigation.findNavController(view)

        if (WebSharedPreferences.getInstance(requireActivity()).isLoggedIn()) {
            getAllFiles()
            val user = WebSharedPreferences.getInstance(requireActivity()).getUser()
            fileRelativeTextView.text = user.username
        }

        filesRecyclerView.adapter = fileAdapter
        filesRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        filesRecyclerView.setHasFixedSize(true)

    }

    override fun onClickListener(fileAttributes: FileAttributes) {
        val user = WebSharedPreferences.getInstance(requireActivity()).getUser()
        getFile(fileAttributes.name, user.password, user.token)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        /*
        inflater.inflate(R.menu.files_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.searchItem)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.background = resources.getDrawable(R.drawable.search_bg, requireContext().theme)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fileAdapter.filter.filter(newText)
                return false
            }
        })

        */
        FilterUtils.onCreateOptionsMenu(requireContext(), resources, menu, inflater) {
            fileAdapter.filter.filter(it)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getFile(name: String, password: String, token: String) {
        val customRequest : CustomRequest<ByteArray> = @RequiresApi(Build.VERSION_CODES.O)
        object : CustomRequest<ByteArray>(Method.GET,
            "${URL.ROOT_FILE}/$name",
            ByteArray::class.java,
            {
                try {
                    if (it != null) {
                        val secretKey: SecretKey = SecretKeySpec(
                            CryptKey.decrypt(
                                password.toByteArray(),
                                CryptKey.secretKey
                            ), "AES"
                        )
                        val decrypted = CryptKey.decrypt(it, secretKey)

                        val outputStream = requireActivity().openFileOutput(
                            name,
                            Context.MODE_PRIVATE
                        )
                        outputStream.write(decrypted)
                        outputStream.close()
                        val file = File(requireActivity().filesDir, name)
                        //CryptKey.decryptFile(file)
                        //Log.i("content", "after->${String(CryptKey.decrypt(it)!!)}")
                        Toast.makeText(activity, "Download $name completed", Toast.LENGTH_LONG)
                            .show()
                        Log.i("file", "content:${file.readText()}")
                    }
                } catch (e: Exception) {
                    Log.e("KEY_ERROR", "UNABLE TO DOWNLOAD FILE")
                    e.printStackTrace()
                }
            },
            {
                Log.e("error get", it.toString())
                WebSharedPreferences.getInstance(requireActivity()).logout()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<ByteArray> {
                return Response.success(
                    response?.data,
                    HttpHeaderParser.parseCacheHeaders(response)
                );
            }
        }

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(customRequest)
    }

    private fun getAllFiles() {
        if (WebSharedPreferences.getInstance(requireActivity()).isLoggedIn()) {
            val user = WebSharedPreferences.getInstance(requireActivity()).getUser()
            LoginRequest.request(requireActivity(), user.username, user.password) {}

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

    private suspend fun updateAdapter(requestBody: String) {
        val files = parse(requestBody)
        fileAdapter.initFullList(files)
        fileAdapter.updateRequests(files)
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
                WebSharedPreferences.getInstance(requireActivity()).logout()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }
        }

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(filesRequest)
    }

}