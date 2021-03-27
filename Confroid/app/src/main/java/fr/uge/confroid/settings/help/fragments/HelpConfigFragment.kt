package fr.uge.confroid.settings.help.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.uge.confroid.R
import fr.uge.confroid.utils.ConfroidAnimationUtils
import kotlinx.android.synthetic.main.fragment_help_config.*


class HelpConfigFragment : Fragment(R.layout.fragment_help_config) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helpPointTrashCanImage.setOnClickListener {
            helpExplainConfigText.text = resources.getString(R.string.delete_config)
            ConfroidAnimationUtils.animationBounce(helpExplainConfigText)
        }

        helpPointBranch.setOnClickListener {
            helpExplainConfigText.text = resources.getString(R.string.branch_details)
            helpConfigImage.setImageResource(R.drawable.branch)
            visibility(true)

        }

        helpPointLeaf.setOnClickListener {
            helpExplainConfigText.text = resources.getString(R.string.modify_config)
            helpConfigImage.setImageResource(R.drawable.leaf)
            visibility(true)
        }

        helpPointGenerateButton.setOnClickListener {
            helpExplainConfigText.text = resources.getString(R.string.generate_modified_config_text)
            ConfroidAnimationUtils.animationBounce(helpExplainConfigText)
        }

        helpConfigBack.setOnClickListener {
            helpConfigImage.setImageResource(R.drawable.config)
            helpExplainConfigText.text = ""
            visibility(false)
        }
    }

    private fun visibility(condition : Boolean) {
        ConfroidAnimationUtils.animationBounce(helpExplainConfigText)
        ConfroidAnimationUtils.animationBounce(helpConfigImage)
        if (condition) {
            helpPointTrashCanImage.visibility = View.INVISIBLE
            helpPointLeaf.visibility = View.INVISIBLE
            helpPointBranch.visibility = View.INVISIBLE
            helpPointGenerateButton.visibility = View.INVISIBLE
            helpConfigBack.visibility = View.VISIBLE
        } else {
            helpPointTrashCanImage.visibility = View.VISIBLE
            helpPointLeaf.visibility = View.VISIBLE
            helpPointBranch.visibility = View.VISIBLE
            helpPointGenerateButton.visibility = View.VISIBLE
            helpConfigBack.visibility = View.INVISIBLE
        }
    }
}