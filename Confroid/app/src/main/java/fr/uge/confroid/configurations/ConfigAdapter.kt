package fr.uge.confroid.configurations


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R
import fr.uge.confroid.utils.CustomDiffUtil
import fr.uge.confroid.utils.FilterUtils

class ConfigAdapter(val listener: AllVersionsFragment, private var listConfigs: List<Config>) :
    RecyclerView.Adapter<ConfigAdapter.ViewHolder>(), Filterable {

    private val listConfigsFull = listConfigs

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val textVersion: TextView = itemView.findViewById(R.id.textVersion)
        private val textTag: TextView = itemView.findViewById(R.id.textTag)

        init {
            itemView.setOnClickListener(this)
        }

        fun update(config: Config) {
            textVersion.text = "VERSION: ${config.version}"
            textTag.text = "TAG: ${config.tag}"
        }

        override fun onClick(v: View?) {
            listener.onClickListener(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_config,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listConfigs[position])
    }

    override fun getItemCount(): Int {
        return listConfigs.size
    }

    private fun updateConfig(newConfigs: List<Config>) {
        val old = listConfigs
        val diff = DiffUtil.calculateDiff(
            CustomDiffUtil(old, newConfigs)
        )
        listConfigs = newConfigs
        diff.dispatchUpdatesTo(this)
    }

    override fun getFilter(): Filter {
        return FilterUtils.filter(
            listConfigsFull,
            {
                "${it.version} ${it.tag}"
            },
            this::updateConfig
        )
    }
}