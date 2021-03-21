package fr.uge.confroid

import android.view.View

object ConfroidAnimationUtils {
    private fun hide(view: View, time: Long) {
        view.animate().apply {
            duration = time
            alpha(0.8f)
        }.withEndAction {
            view.animate().apply {
                duration = time
                alpha(0f)
                view.visibility = View.INVISIBLE
            }
        }

    }

    private fun reveal(view: View, time: Long) {
        view.animate().apply {
            duration = time
            alpha(0.2f)
        }.withEndAction {
            view.animate().apply {
                duration = time
                alpha(1f)
                view.visibility = View.VISIBLE
            }
        }
    }

    fun animationVisibility(view: View, revealTime : Long, hideTime : Long) {
        if (view.visibility == View.INVISIBLE) {
            reveal(view, revealTime)
        } else {
            hide(view, hideTime)
        }
    }
}