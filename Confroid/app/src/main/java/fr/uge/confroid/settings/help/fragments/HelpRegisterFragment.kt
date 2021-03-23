package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.utils.ConfroidAnimationUtils
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_help_register.*

class HelpRegisterFragment : Fragment(R.layout.fragment_help_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView2.movementMethod = ScrollingMovementMethod()

        imageView2.setOnClickListener {
            ConfroidAnimationUtils.animationVisibility(imageView3, 300, 600)
            ConfroidAnimationUtils.animationVisibility(textView2, 600, 300)
        }
    }
}