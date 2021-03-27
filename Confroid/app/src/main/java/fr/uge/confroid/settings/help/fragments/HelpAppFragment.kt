package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.R
import fr.uge.confroid.utils.ConfroidAnimationUtils
import kotlinx.android.synthetic.main.fragment_help_app.*

class HelpAppFragment : Fragment(R.layout.fragment_help_app) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpAppExplainText.movementMethod = ScrollingMovementMethod()

        helpPointNumberOfVersionImage.setOnClickListener {
            helpAppExplainText.text = resources.getString(R.string.number_of_versions_text)
            ConfroidAnimationUtils.animationBounce(helpAppExplainText)
        }

        helpPointAppImage.setOnClickListener {
            helpAppExplainText.text = resources.getString(R.string.help_app_explain)
            ConfroidAnimationUtils.animationBounce(helpAppExplainText)

        }
    }
}