package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.R
import fr.uge.confroid.utils.ConfroidAnimationUtils
import kotlinx.android.synthetic.main.fragment_help_connected.*

/***
 * Will help the user understand better when the user is log in.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class HelpConnectedFragment : Fragment(R.layout.fragment_help_connected) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helpPointFilesButton.setOnClickListener {
            visibility(true)
        }

        helpFilesBack.setOnClickListener {
            visibility(false)
        }
    }

    private fun visibility(isVisible : Boolean) {
        if (isVisible) {
            helpFilesBack.visibility = View.VISIBLE
            helpPointFilesButton.visibility = View.INVISIBLE
            helpFilesImage.setImageResource(R.drawable.files)
        } else {
            helpFilesBack.visibility = View.INVISIBLE
            helpPointFilesButton.visibility = View.VISIBLE
            helpFilesImage.setImageResource(R.drawable.menu_log_in)
        }
        ConfroidAnimationUtils.animationBounce(helpFilesImage)
    }
}