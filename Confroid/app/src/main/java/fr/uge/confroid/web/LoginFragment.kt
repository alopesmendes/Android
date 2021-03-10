package fr.uge.confroid.web

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import fr.uge.confroid.MainActivity
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*


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

    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        buttonLogin.setOnClickListener {
            userLogin()
        }

        textViewRegisterNowLogin.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
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

        LoginRequest.request(requireActivity(), username, String(cryptPassword!!)) {
            //Intent(activity, MainActivity::class.java).apply { startActivity(this) }
            navController.navigate(R.id.action_loginFragment_to_appFragment)
            mainDrawerLayout.invalidate()

        }
    }
}