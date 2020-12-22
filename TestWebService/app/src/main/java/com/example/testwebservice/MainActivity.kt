package com.example.testwebservice

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    private val requestAdapter : RequestAdapter = RequestAdapter(ArrayList())
    private var searchTask : SearchTask =  SearchTask()

    private fun execute(it : Any) {
        if (searchTask == null || searchTask.isCancelled || searchTask.status == AsyncTask.Status.FINISHED) {
            searchTask = SearchTask()
            searchTask.execute(it)
        } else {
            searchTask.cancel(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGetRequest.setOnClickListener {
            var customRequest = CustomRequest(Request.Method.GET, url, Any::class.java,
                    {
                        execute(it)
                    },
                    {
                        Log.i("error", it.toString())
                    }
            )

            MySingleton.getInstance(this).addToRequestQueue(customRequest)
        }

        buttonPostRequest.setOnClickListener {
            /*
            val request = object : CustomRequest<Any>(Method.POST, url, Any::class.java,
                    {

                    },
                    {
                        Log.e("error", it.toString())
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = java.util.HashMap()
                    params["id"] = "5"
                    params["name"] = "japanenglish"
                    params["vtuber"] = listOf("korone", "okayu", "aqua").toString()
                    return params
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = java.util.HashMap()
                    params["Content-Type"] = "application/x-www-form-urlencoded"
                    return params
                }
            }
            MySingleton.getInstance(this).addToRequestQueue(request)
            */
            //sendRequest()
        }

        recyclerView.adapter = requestAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    /***
     * Will post a random JSONObject
     * The key is always a string.
     * Thr value can be all primitives objects, String, JSONObject, JSONArray and Null
     */
    private fun sendRequest() {
        val jsonBody = JSONObject()
        jsonBody.put("id", 5)
        jsonBody.put("name", "japanenglish")
        jsonBody.put("vtuber", JSONArray(listOf("korone", "okayu", "aqua")))

        var request = JsonObjectRequest(Request.Method.POST, url, jsonBody,
                {
                    Log.i("success", it.toString())
                },
                {
                    Log.e("error", it.toString())
                }
        )
        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    companion object {
        private var url = "https://valkyroid.serverless.social/api/courses"
    }


    /***
     * The async task that will display as a recycler view the elements to see...
     */
    inner class SearchTask : AsyncTask<Any, Int, Content>() {
        override fun doInBackground(vararg params: Any?): Content {
            var content = params[0]?.let { Content(it) }
            content?.convertToBundle()
            return content!!
        }

        override fun onPostExecute(result: Content?) {
            super.onPostExecute(result)
            requestAdapter.requests = listOf(result!!)
            requestAdapter.notifyDataSetChanged()

        }


    }
}