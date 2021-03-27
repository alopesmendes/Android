package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.utils.ConfroidAnimationUtils
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_help_app.*
import kotlinx.android.synthetic.main.fragment_help_login.*

/***
 * Will help the user understand better the LoginFragment.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class HelpLoginFragment : Fragment(R.layout.fragment_help_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpExplainLoginText.movementMethod = ScrollingMovementMethod()

        helpLoginButton.setOnClickListener {
            helpExplainLoginText.text = resources.getString(R.string.help_explain_login)
            ConfroidAnimationUtils.animationBounce(helpExplainLoginText)
        }

        helpPointRegisterTextOfLogin.setOnClickListener {
            helpExplainLoginText.text = resources.getString(R.string.help_explain_register_button)
            ConfroidAnimationUtils.animationBounce(helpExplainLoginText)
        }
    }
}