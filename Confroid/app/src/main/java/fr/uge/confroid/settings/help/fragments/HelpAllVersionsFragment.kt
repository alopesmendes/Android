package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.R
import fr.uge.confroid.utils.ConfroidAnimationUtils
import kotlinx.android.synthetic.main.fragment_help_all_versions.*

/***
 * Will help the user understand better the AllVersionsFragment.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class HelpAllVersionsFragment : Fragment(R.layout.fragment_help_all_versions) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpExplainAllVersionText.movementMethod = ScrollingMovementMethod()

        helpPointAllVersionImage.setOnClickListener {
            ConfroidAnimationUtils.animationVisibility(helpExplainAllVersionImage, 300, 600)
            ConfroidAnimationUtils.animationVisibility(helpExplainAllVersionText, 600, 300)
        }
    }
}