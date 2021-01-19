package fr.uge.confroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import fr.uge.confroid.web.URL
import fr.uge.confroid.web.VolleySingleton
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            userLogin()
        }

        textViewRegisterNowLogin.setOnClickListener {
            Intent(this, RegisterActivity::class.java).apply { startActivity(this) }
        }
    }

    private fun userLogin() {

        val username = editTextNameLogin.text.toString()
        val password = editTextPasswordLogin.text.toString()

        if (TextUtils.isEmpty(username)) {
            editTextNameLogin.error = "Please enter your username"
            editTextNameLogin.requestFocus()
            return
        }

        if (TextUtils.isEmpty(password)) {
            editTextPasswordLogin.error = "Please enter your password"
            editTextPasswordLogin.requestFocus()
            return
        }

        val customRequest = object : StringRequest(Method.POST, URL.ROOT_URL,
            {
                Log.i("good", it.toString())
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