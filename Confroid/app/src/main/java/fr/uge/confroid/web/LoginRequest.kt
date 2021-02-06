package fr.uge.confroid.web

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

object LoginRequest {

    fun request(applicationContext : Context, username : String, password : String, callback : () -> Unit) {
        val customRequest = object : CustomRequest<User>(Method.POST, URL.ROOT_LOGIN, User::class.java,
            {
                SharedPreferences.getInstance(applicationContext).userLogin(it)
                callback()
            },
            {
                Log.i("bad", it.toString())
            }
        ) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }

        VolleySingleton.getInstance(applicationContext).addToRequestQueue(customRequest)
    }
}