package fr.uge.confroid.utils

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtils {
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