package fr.uge.confroid.utils

import android.view.View
import android.view.animation.BounceInterpolator

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

    fun animationBounce(view: View) {
        view.animate().apply {
            interpolator = BounceInterpolator()
            scaleX(0.1f)
            scaleXBy(0.3f)
            scaleY(0.1f)
            scaleYBy(0.3f)
            duration = 500
        }.withEndAction {
            view.animate().apply {
                scaleX(-0.1f)
                scaleXBy(-0.3f)
                scaleY(-0.1f)
                scaleYBy(-0.3f)
            }
        }
    }
}