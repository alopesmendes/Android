package fr.uge.confroid.settings

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.country_spinner_row.*
import kotlinx.android.synthetic.main.country_spinner_row.view.*


class LanguageAdapter(val context: Context, var languageNameList: Array<String>, var flagList: TypedArray) : BaseAdapter() {

    override fun getCount(): Int {
        return languageNameList.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }


    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val name = languageNameList[position]

        return LayoutInflater.from(context).inflate(R.layout.country_spinner_row, null).apply {
            countryName.text = name.toString()
            countryFlag.setImageDrawable(flagList.getDrawable(position))
        }
    }

}