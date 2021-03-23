package fr.uge.confroid.settings.help.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import fr.uge.confroid.utils.ConfroidAnimationUtils
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_help_menu.*


class HelpMenuFragment : Fragment(R.layout.fragment_help_menu) {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helpPointToolBarImage.setOnClickListener {

            ConfroidAnimationUtils.animationVisibility(helpMenuImage, 300, 300)

        }
    }
}