package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.utils.ConfroidAnimationUtils
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_help_login.*


class HelpLoginFragment : Fragment(R.layout.fragment_help_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpExplainLoginText.movementMethod = ScrollingMovementMethod()

        helpLoginButton.setOnClickListener {
            ConfroidAnimationUtils.animationVisibility(helpExplainLoginText, 600, 600)
            helpExplainLoginText.text = resources.getString(R.string.help_explain_login)
        }

        helpPointRegisterTextOfLogin.setOnClickListener {
            ConfroidAnimationUtils.animationVisibility(helpExplainLoginText, 600, 600)
            helpExplainLoginText.text = resources.getString(R.string.help_explain_register_button)
        }
    }
}