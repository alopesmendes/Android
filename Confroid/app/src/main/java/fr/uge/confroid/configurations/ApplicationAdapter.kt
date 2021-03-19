package fr.uge.confroid.configurations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.application_view.view.*

class ApplicationAdapter(
    private val listApplications: List<Application>,
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
            configCount.text = "${application.configCount} configurations"
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
            is ViewHolder -> {holder.update(listApplications[position])}
        }
    }

    override fun getItemCount(): Int {
        return listApplications.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}