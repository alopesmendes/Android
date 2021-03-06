package fr.uge.confroid.web

import android.content.Context

/**
 * This class allow us to deal with the users data and check if they are login.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class WebSharedPreferences(private val context: Context) {
    companion object {
        @Volatile
        private var INSTANCE : WebSharedPreferences? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: WebSharedPreferences(context).also {
                INSTANCE = it
            }
        }

        private const val SHARED_PREFS = "SHARED_PREFS"
        private const val KEY_USERNAME = "keyusername"
        private const val KEY_PASSWORD = "keypassword"
        private const val KEY_TOKEN = "keytoken"
    }

    /***
     * Method will allow the user to login.
     * Will store the user data in shared preferences.
     *
     * @param user : Will log in the user.
     */
    public fun userLogin(user: User) {
        val sharedPreferences = INSTANCE?.context?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(KEY_USERNAME, user.username)
        editor?.putString(KEY_PASSWORD, user.password)
        editor?.putString(KEY_TOKEN, user.token)
        editor?.apply()
    }

    /**
     * Will verify the user is logged in.
     *
     * @return Will return true if a user is log in.
     */
    public fun isLoggedIn() : Boolean {
        val sharedPreferences = INSTANCE?.context?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(KEY_USERNAME, null) != null
    }

    /**
     * @return Will return the current user.
     */
    public fun getUser() : User {
        val sharedPreferences = INSTANCE?.context?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return User(
            sharedPreferences?.getString(KEY_USERNAME, null)!!,
            sharedPreferences?.getString(KEY_PASSWORD, null)!!,
            sharedPreferences?.getString(KEY_TOKEN, "")!!
        )
    }

    /**
     * Will log out an user and bring the program to the @see LoginActivity.
     */
    public fun logout() {
        val sharedPreferences = INSTANCE?.context?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
        //INSTANCE?.context?.startActivity(Intent(INSTANCE?.context, MainActivity::class.java))

    }
}