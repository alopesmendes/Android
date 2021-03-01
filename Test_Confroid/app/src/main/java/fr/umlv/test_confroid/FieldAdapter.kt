package fr.umlv.test_confroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FieldAdapter(private val listConfigs: List<Field>) : RecyclerView.Adapter<FieldAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val nameField : TextView = itemView.findViewById(R.id.textVersion)
        private val textTag : TextView = itemView.findViewById(R.id.textTag)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.field,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FieldAdapter.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return listConfigs.size
    }
}