package fr.uge.confroid.configurations.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.utils.CustomDiffUtil
import fr.uge.confroid.R
import fr.uge.confroid.utils.FilterUtils
import kotlinx.android.synthetic.main.application_view.view.*

class ApplicationAdapter(
    var listApplications: List<Application>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ApplicationAdapter.ViewHolder>(), Filterable {

    private val listApplicationsFull = listApplications

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val appName: TextView = itemView.appNameTextView
        private val configCount: TextView = itemView.configCountTextView

        init {
            itemView.setOnClickListener(this)
        }

        fun update(application: Application) {
            appName.text = application.name
            val count = application.configCount
            configCount.text = "$count"

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
        holder.update(listApplications[position])
    }

    override fun getItemCount(): Int {
        return listApplications.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun updateApps(newApps: List<Application>) {
        val old = listApplications
        val diff = DiffUtil.calculateDiff(
            CustomDiffUtil(old, newApps)
        )
        listApplications = newApps
        diff.dispatchUpdatesTo(this)
    }

    override fun getFilter(): Filter {
        return FilterUtils.filter(listApplicationsFull, { it.toString() }, this::updateApps)
    }
}