package fr.uge.confroid.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.uge.confroid.R

class HelpPageAdapter(var helpItems : List<HelpItem>) : RecyclerView.Adapter<HelpPageAdapter.ViewHolder>() {
    inner class ViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        private val title : TextView  = item.findViewById(R.id.titleHelpItem)
        private val image : ImageView = item.findViewById(R.id.helpImage)
        private val description : TextView = item.findViewById(R.id.descriptionHelpItem)

        fun update(helpItem: HelpItem) {
            title.text = helpItem.title
            image.setImageResource(helpItem.image)
            description.text =  helpItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.help_item_view,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(helpItems[position])
    }

    override fun getItemCount(): Int {
        return helpItems.size
    }
}