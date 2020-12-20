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
            val stringRequest = object : CustomRequest<Any>(Method.POST, url, Any::class.java,
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
                    params["vtuber"] = "korone"
                    return params
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = java.util.HashMap()
                    params["Content-Type"] = "application/x-www-form-urlencoded"
                    return params
                }
            }
            MySingleton.getInstance(this).addToRequestQueue(stringRequest)

            //sendRequest()
        }

        recyclerView.adapter = requestAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }


    private fun sendRequest() {
        val jsonBody = JSONObject()
        jsonBody.put("id", 5)
        jsonBody.put("name", "japanenglish")
        jsonBody.put("vtuber", "korone")
        val requestBody = jsonBody.toString()

        var request = object : StringRequest(Method.POST, url,
                {
                    val jsonObject = JSONObject(it)
                    Log.i("success", jsonObject.toString())
                },
                {
                    Log.e("error", it.toString())
                }
        ) {
            override fun getBodyContentType(): String {

                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                try {
                    return requestBody.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                    Log.e("error byte", e.toString())
                    throw e
                }
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                var json = String(response!!.data, Charset.forName(HttpHeaderParser.parseCharset(response!!.headers)))
                return Response.success(json, HttpHeaderParser.parseCacheHeaders(response))
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    companion object {
        private var url = "https://fagaj.loca.lt/api/courses"
    }

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