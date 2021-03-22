package fr.uge.confroid.web

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.CustomDiffUtil
import fr.uge.confroid.R

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

    /*
    inner class FileDiffUtil(
        private val old: List<FileAttributes>,
        private val current: List<FileAttributes>
    ) : DiffUtil.Callback() {
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
    */

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
        return filter
    }

    private val filter : Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<FileAttributes>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(requestsFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in requestsFull) {
                    if (item.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val results = results?.values as List<FileAttributes>
            if (results == null) {
                updateRequests(requests)
            } else {
                updateRequests(results)
            }
        }

    }
}