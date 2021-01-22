package fr.uge.confroid.web

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import fr.uge.confroid.MainActivity
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * This activity will allow the user to log in.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            userLogin()
            Intent(this, MainActivity::class.java).apply { startActivity(this) }
        }

        textViewRegisterNowLogin.setOnClickListener {
            Intent(this, RegisterActivity::class.java).apply { startActivity(this) }
        }
    }

    private fun isMissingFields(username : String, password : String) : Boolean {
        if (TextUtils.isEmpty(username)) {
            editTextNameLogin.error = "Please enter your username"
            editTextNameLogin.requestFocus()
            return true
        }

        if (TextUtils.isEmpty(password)) {
            editTextPasswordLogin.error = "Please enter your password"
            editTextPasswordLogin.requestFocus()
            return true
        }
        return false
    }

    private fun userLogin() {

        val username = editTextNameLogin.text.toString()
        val password = editTextPasswordLogin.text.toString()

        if (isMissingFields(username, password)) {
            return
        }
        val customRequest = object : CustomRequest<User>(Method.POST, URL.ROOT_URL, User::class.java,
            {
                Log.i("good", it.toString())
                SharedPreferences.getInstance(applicationContext).userLogin(it)

            },
            {
                Log.i("bad", it.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(customRequest)
    }
}