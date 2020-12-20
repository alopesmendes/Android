package com.example.testwebservice

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import org.json.JSONException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


open class CustomRequest<T>(
        requestMethod: Int,
        url: String,
        private val clazz: Class<T>,
        private val listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
) : Request<T>(requestMethod, url, errorListener) {

    private val gson : Gson = Gson()

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {

            var json = String(response!!.data, Charset.forName(HttpHeaderParser.parseCharset(response!!.headers)))
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response))

        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }
    }

    override fun deliverResponse(response: T) {
        listener.onResponse(response)
    }


}
