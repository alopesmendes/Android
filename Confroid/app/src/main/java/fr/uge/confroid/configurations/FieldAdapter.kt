package fr.uge.confroid.configurations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R

class FieldAdapter(private val listFields: List<Field>) : RecyclerView.Adapter<FieldAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val nameField : TextView = itemView.findViewById(R.id.nameField)
        private val valueField : TextView = itemView.findViewById(R.id.valueField)
        private val rvRecField: RecyclerView = itemView.findViewById(R.id.RvValueField)


        init {
            itemView.setOnClickListener(this)
        }

        fun update(field: Field){
            nameField.text = field.name
            valueField.text = field.content
            var fieldAdapter = field.recursiveContent?.let { FieldAdapter(it) }
            rvRecField.adapter = fieldAdapter
            rvRecField.layoutManager = LinearLayoutManager(
                rvRecField.context,
                LinearLayoutManager.VERTICAL,
                false
            );
        }

        override fun onClick(v: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.field,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listFields[position])
    }

    override fun getItemCount(): Int {
        return listFields.size
    }
}