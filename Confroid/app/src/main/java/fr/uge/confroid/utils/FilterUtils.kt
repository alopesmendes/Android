package fr.uge.confroid.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.SearchView
import androidx.annotation.RequiresApi
import fr.uge.confroid.R

object FilterUtils {

    fun <T> filter(list : List<T>, updateRequests : (newList: List<T>) -> Unit) : Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<T>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(list)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in list) {
                        if (item.toString().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val results = results?.values as List<T>
                if (results == null) {
                    updateRequests(list)
                } else {
                    updateRequests(results)
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onCreateOptionsMenu(context : Context, resources: Resources, menu: Menu, inflater: MenuInflater, callback : (text : String) -> Unit) {
        inflater.inflate(R.menu.files_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.searchItem)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.background = resources.getDrawable(R.drawable.search_bg, context.theme)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    callback(newText)
                }
                return false
            }
        })
    }
}