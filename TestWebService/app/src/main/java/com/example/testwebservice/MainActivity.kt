package com.example.testwebservice

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private val requestAdapter : RequestAdapter = RequestAdapter(ArrayList())
    private val searchTask : SearchTask =  SearchTask()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonValidate.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(Request.Method.GET, url,
                    {
                        searchTask.execute(it)
                    },
                    {
                        Log.e("url error", it.toString())
                    })

            // Add the request to the RequestQueue.
            queue.add(stringRequest)

        }

        recyclerView.adapter = requestAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    data class SearchResultEntry(val id : String, val title : String)

    companion object {
        private var url = "http://192.168.1.17:8080/api/courses"
    }

    private fun parseSearchResult(resultPage : String) : List<SearchResultEntry> {
        val obj = Gson().fromJson(resultPage, List::class.java)
        return obj.map {
            val e = it as Map<String, Any>
            SearchResultEntry(
                    e["id"].toString(),
                    e["name"].toString()
            )
        }
    }

    inner class SearchTask : AsyncTask<String, Int, List<SearchResultEntry>>() {
        override fun doInBackground(vararg params: String?): List<SearchResultEntry>? {
            return params[0]?.let { parseSearchResult(it) }
        }

        override fun onPostExecute(result: List<SearchResultEntry>?) {
            super.onPostExecute(result)
            requestAdapter.requests = result!!
            requestAdapter.notifyDataSetChanged()
        }
    }
}