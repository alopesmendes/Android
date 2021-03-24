package fr.uge.confroid.web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.utils.CustomDiffUtil
import fr.uge.confroid.R
import fr.uge.confroid.utils.FilterUtils

class FileAdapter(val listener: OnFileListener, var requests: List<FileAttributes>) : RecyclerView.Adapter<FileAdapter.ViewHolder>(), Filterable {
    private lateinit var requestsFull : List<FileAttributes>

    inner class ViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView), View.OnClickListener {
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

    fun initFullList(fileAttributes: List<FileAttributes>) {
        requestsFull = fileAttributes.toList()
    }

    fun updateRequests(fileAttributes: List<FileAttributes>) {
        val old = requests
        val diff = DiffUtil.calculateDiff(
            CustomDiffUtil(old, fileAttributes)
        )
        requests = fileAttributes
        diff.dispatchUpdatesTo(this)
    }

    override fun getFilter(): Filter {
        return FilterUtils.filter(requestsFull, { it.name }, this::updateRequests)
    }

}