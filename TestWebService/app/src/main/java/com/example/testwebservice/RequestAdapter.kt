package com.example.testwebservice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/***
 * The RequestAdapter to see...
 */
class RequestAdapter(var requests : List<Content>) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitleView : TextView = itemView.findViewById(R.id.textTitle)

        fun update(search: Content) {
            textTitleView.text = "${search.content}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.activity_card,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(requests[position])
    }

    override fun getItemCount(): Int {
        return requests.size
    }
}