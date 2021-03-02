package fr.uge.confroid.web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R

class FileAdapter(val listener: OnFileListener, var requests: List<FileAttributes>) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textView : TextView = itemView.findViewById(R.id.textCardView)
        private val constraintLayout : ConstraintLayout = itemView.findViewById(R.id.fileCardConstraintLayout)

        init {
            constraintLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClickListener(requests[adapterPosition])
        }

        fun update(fileAttributes: FileAttributes) {
            textView.text = fileAttributes.name
        }
    }

    interface OnFileListener {
        fun onClickListener(fileAttributes: FileAttributes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_file,
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