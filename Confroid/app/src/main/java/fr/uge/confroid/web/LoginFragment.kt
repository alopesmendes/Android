package fr.uge.confroid.web

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import fr.uge.confroid.R
import fr.uge.confroid.web.LoginRequest.logAction
import fr.uge.confroid.worker.UploadWorker
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit


class LoginViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<LoginFragment>()
    val selectedItem: LiveData<LoginFragment> get() = mutableSelectedItem

    fun selectItem(item: LoginFragment) {
        mutableSelectedItem.value = item
    }

}

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
    private val viewModel: LoginViewModel by activityViewModels()

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
            viewModel.selectItem(this)
            navController.navigate(R.id.action_loginFragment_to_appFragment)
            logAction(requireContext())
            //mainDrawerLayout.invalidate()

        }
    }
}