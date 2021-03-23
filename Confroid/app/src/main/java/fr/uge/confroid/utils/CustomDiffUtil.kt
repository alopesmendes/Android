package fr.uge.confroid.utils

import androidx.recyclerview.widget.DiffUtil

class CustomDiffUtil(private val old: List<*>, private val current: List<*>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return current.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == current[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == current[newItemPosition]
    }
}