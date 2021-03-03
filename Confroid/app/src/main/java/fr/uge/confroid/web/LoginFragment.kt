package fr.uge.confroid.web

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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
class LoginFragment : Fragment(R.layout.fragment_login) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transaction = activity!!.supportFragmentManager.beginTransaction().addToBackStack(null)

        buttonLogin.setOnClickListener {
            userLogin()
        }

        textViewRegisterNowLogin.setOnClickListener {
            transaction.replace(R.id.mainFrameLayout, LoginFragment()).commit()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun userLogin() {
        val username = editTextNameLogin.text.toString()
        val password = editTextPasswordLogin.text.toString()

        if (isMissingFields(username, password)) {
            return
        }
        val cryptPassword = CryptKey.encrypt(password.toByteArray(), CryptKey.secretKey, 2)

        val transaction = activity!!.supportFragmentManager.beginTransaction().addToBackStack(null)

        LoginRequest.request(activity!!, username, String(cryptPassword!!)) {
            transaction.replace(R.id.mainFrameLayout, AppFragment()).commit()
        }
    }

}