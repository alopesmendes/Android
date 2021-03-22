package fr.uge.confroid.configurations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.application_view.view.*

class ApplicationAdapter(
    private var listApplications: List<Application>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ApplicationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val appName = itemView.appNameTextView
        val configCount = itemView.configCountTextView

        init {
            itemView.setOnClickListener(this)
        }

        fun update(application: Application) {
            appName.text = application.name
            val count = application.configCount
            if (count > 1) {
                configCount.text = "$count configurations"
            } else {
                configCount.text = "$count configuration"
            }

        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.application_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                holder.update(listApplications[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listApplications.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class AppDiffUtil(
        private val old: List<Application>,
        private val current: List<Application>
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

    fun updateApps(newApps: List<Application>) {
        val old = listApplications
        val diff = DiffUtil.calculateDiff(
            AppDiffUtil(old, newApps)
        )
        listApplications = newApps
        diff.dispatchUpdatesTo(this)
    }
}