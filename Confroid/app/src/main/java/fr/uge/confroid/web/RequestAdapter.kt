package fr.uge.confroid.web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R

class RequestAdapter(val listener: OnUrlListener, var fas: List<FileAttributes>) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textName:TextView = itemView.findViewById(R.id.cardTextName)
        private val textUrl:TextView = itemView.findViewById(R.id.cardTextUrl)

        init {
            textUrl.setOnClickListener(this)
        }

        fun update(fileAttributes: FileAttributes) {
            textName.text = fileAttributes.name
            textUrl.text = fileAttributes.url
        }

        override fun onClick(v: View?) {
            listener.onClickListener(fas[adapterPosition])
        }

    }

    interface OnUrlListener {
        fun onClickListener(fileAttributes: FileAttributes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_card_request,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(fas[position])
    }

    override fun getItemCount(): Int {
        return fas.size
    }
}