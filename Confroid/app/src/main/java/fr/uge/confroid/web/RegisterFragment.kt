package fr.uge.confroid.web

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * This activity will allow the user to register
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class RegisterFragment : Fragment(R.layout.fragment_register) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegister.setOnClickListener {
            userRegister()
        }
    }



    private fun isMissingFields(username : String, password : String, confirmPassword : String) : Boolean {
        if (TextUtils.isEmpty(username)) {
            editTextNameRegister.error = "Please enter your username"
            editTextNameRegister.requestFocus()
            return true
        }

        if (TextUtils.isEmpty(password)) {
            editTextPasswordRegister.error = "Please enter your password"
            editTextPasswordRegister.requestFocus()
            return true
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPasswordRegister.error = "Please enter confirm password"
            editTextConfirmPasswordRegister.requestFocus()
            return true
        }

        if (password != confirmPassword) {
            editTextConfirmPasswordRegister.error = "Different from password"
            editTextConfirmPasswordRegister.requestFocus()
            return true
        }
        return false
    }

    private fun userRegister() {
        val username = editTextNameRegister.text.toString()
        val password = editTextPasswordRegister.text.toString()
        val confirmPassword = editTextConfirmPasswordRegister.text.toString()

        val transaction = activity!!.supportFragmentManager.beginTransaction().addToBackStack(null)

        if (isMissingFields(username, password, confirmPassword)) {
            return
        }
        val customRequest = object : CustomRequest<User>(
            Method.POST, URL.ROOT_REGISTER, User::class.java,
            {
                Log.i("good", it.toString())
                //SharedPreferences.getInstance(applicationContext).userLogin(it)
                transaction.replace(R.id.mainFrameLayout, LoginFragment()).commit()

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

        VolleySingleton.getInstance(activity!!).addToRequestQueue(customRequest)
    }
}