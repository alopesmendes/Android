package fr.uge.confroid.configurations

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.CustomDiffUtil
import fr.uge.confroid.R


class FieldAdapter(val listener: ConfigFragment, private var listFields: ArrayList<Field>) : RecyclerView.Adapter<FieldAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val nameField : TextView = itemView.findViewById(R.id.nameField)
        private val valueField : TextView = itemView.findViewById(R.id.valueField)


        init {
            itemView.setOnClickListener(this)
        }

        fun update(field: Field){
            nameField.text = field.name
            if (field.recursiveContent.isNullOrEmpty()){
                valueField.text = field.content
            }else{
                valueField.text = "[Click to show details]"
            }
        }

        override fun onClick(v: View?) {
            listener.onClickListener(adapterPosition)
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
        Log.i("values $position", "${listFields[position]}")
        holder.update(listFields[position])
    }

    override fun getItemCount(): Int {
        return listFields.size
    }

    fun updateRequests(fields: ArrayList<Field>) {
        val old = listFields
        val diff = DiffUtil.calculateDiff(
            CustomDiffUtil(old, fields)
        )
        listFields = fields
        diff.dispatchUpdatesTo(this)
    }

    fun updatevalue(positionToChange: Int, field: Field?) {
        if (field != null) {
            listFields[positionToChange] = field
        }
    }

    fun getfields(): ArrayList<Field> {
        return listFields
    }

    fun getfield(position: Int): Field{
        return listFields[position]
    }


}