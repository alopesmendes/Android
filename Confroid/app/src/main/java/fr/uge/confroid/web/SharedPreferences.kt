package fr.uge.confroid.web

import android.content.Context

class SharedPreferences(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE : SharedPreferences? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: SharedPreferences(context).also {
                INSTANCE = it
            }
        }
    }


}