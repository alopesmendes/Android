package fr.uge.confroid.utils

import android.view.View
import android.widget.Filter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/***
 * Will deal with the features of an [RecyclerView].
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
object RecyclerViewUtils {

    /***
     * Determines it the [recyclerView] or [imageLogo] is visible.
     *
     * @param isVisible a [Boolean] determining if the [RecyclerView] is visible or not.
     * @param recyclerView the [RecyclerView].
     * @param imageLogo the [ImageView] of the logo.
     */
    fun visibility(isVisible: Boolean, recyclerView: RecyclerView, imageLogo: ImageView) {
        if (isVisible) {
            recyclerView.visibility = View.INVISIBLE
            imageLogo.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            imageLogo.visibility = View.INVISIBLE
        }
    }
}