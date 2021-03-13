package fr.uge.confroid.configurations


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R

class ConfigAdapter (val listener: AllVersionsFragment, private val listConfigs: List<Config>):RecyclerView.Adapter<ConfigAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val textVersion : TextView = itemView.findViewById(R.id.textVersion)
        private val textTag : TextView = itemView.findViewById(R.id.textTag)

        init {
            itemView.setOnClickListener(this)
        }

        fun update(config: Config){
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
}