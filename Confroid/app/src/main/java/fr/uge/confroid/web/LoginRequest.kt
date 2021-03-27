package fr.uge.confroid.web

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import fr.uge.confroid.worker.UploadWorker
import java.util.concurrent.TimeUnit


/**
 * This activity will deal with the features of a login request.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
object LoginRequest {

    /***
     * Will launch a login request.
     * @param applicationContext the [Context].
     * @param username the user name of an user.
     * @param password the password of an user.
     * @param callback a function that takes no parameter and returns [Unit].
     */
    fun request(applicationContext: Context, username: String, password: String, callback: () -> Unit) {
        val customRequest = object : CustomRequest<User>(Method.POST, URL.ROOT_LOGIN, User::class.java,
            {
                WebSharedPreferences.getInstance(applicationContext).userLogin(it)
                callback()
            },
            {
                Log.i("bad", it.toString())
                WebSharedPreferences.getInstance(applicationContext).logout()
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

    /***
     * Will launch a log request then call [work] if the user is log in.
     * @param context the [Context]
     */
    fun logAction(context: Context) {
        if (WebSharedPreferences.getInstance(context).isLoggedIn()) {

            val user = WebSharedPreferences.getInstance(context).getUser()
            LoginRequest.request(context, user.username, user.password) {}
            work(context)

            Log.i("shared user", user.toString())

        }
    }

    /***
     * Will create the worker and launch it.
     * @param context the [Context].
     */
    fun work(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val work = PeriodicWorkRequestBuilder<UploadWorker>(20, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(work)
    }
}