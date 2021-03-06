package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.utils.ConfroidAnimationUtils
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_help_register.*

/***
 * Will help the user understand better the RegisterFragment.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class HelpRegisterFragment : Fragment(R.layout.fragment_help_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpExplainRegisterText.movementMethod = ScrollingMovementMethod()

        helpRegisterButtonImage.setOnClickListener {
            ConfroidAnimationUtils.animationVisibility(helpExplainRegisterImage, 300, 600)
            ConfroidAnimationUtils.animationVisibility(helpExplainRegisterText, 600, 300)
        }
    }
}