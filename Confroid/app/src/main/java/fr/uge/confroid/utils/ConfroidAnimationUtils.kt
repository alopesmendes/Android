package fr.uge.confroid.utils

import android.view.View
import android.view.animation.BounceInterpolator

/***
 * Will deal with the features of an Animation.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
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

    /***
     * The animation of visibility.
     * @param view a [View].
     * @param revealTime the time to reveal the [View].
     * @param hideTime the time to hide the [View].
     */
    fun animationVisibility(view: View, revealTime : Long, hideTime : Long) {
        if (view.visibility == View.INVISIBLE) {
            reveal(view, revealTime)
        } else {
            hide(view, hideTime)
        }
    }

    /***
     * The animation of a bounce.
     * @param view the [View].
     */
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