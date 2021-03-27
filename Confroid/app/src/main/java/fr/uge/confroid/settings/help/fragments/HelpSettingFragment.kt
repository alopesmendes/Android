package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.utils.ConfroidAnimationUtils
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_help_setting.*

/***
 * Will help the user understand better the SettingFragment.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class HelpSettingFragment : Fragment(R.layout.fragment_help_setting) {
    private var isNight = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpPointTheme.setOnClickListener {

            if (isNight) {
                helpSettingsImage.setImageResource(R.drawable.settings_light)
            } else {
                helpSettingsImage.setImageResource(R.drawable.settings_night)
            }
            isNight = !isNight
        }

        helpPointLanguages.setOnClickListener {
            ConfroidAnimationUtils.animationVisibility(helpImageExplainLanguage, 300, 600)
            ConfroidAnimationUtils.animationVisibility(helpExplainLanguageSetting, 600, 300)
        }
    }

}