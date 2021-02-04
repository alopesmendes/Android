package fr.uge.confroid.web

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.toolbox.StringRequest
import fr.uge.confroid.MainActivity
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

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
        val customRequest = object : StringRequest(Method.POST, URL.ROOT_LOGIN,
            {
                val jsonObject = JSONObject(it)
                val user = User(jsonObject.getString("username"), jsonObject.getString("password"), jsonObject.getString("token"))
                Log.i("user", user.toString())
                SharedPreferences.getInstance(applicationContext).userLogin(user)
                Intent(this, MainActivity::class.java).apply { startActivity(this) }

            },
            {
                Log.i("bad", it.toString())
            }
        ) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                val cryptPassword = CryptKey.encrypt(password.toByteArray(), CryptKey.secretKey, 2)
                params["username"] = username
                params["password"] = String(cryptPassword!!)
                //params["password"] = password
                return params
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(customRequest)
    }
}