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

/***
 * Will deal with the features of an [Filter].
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
object FilterUtils {

    /***
     * Will create a filter from the list given.
     * Will use [transformString] to filter the pattern.
     * Will use [updateRequests] to update the list of the requests so the filter can happen.
     *
     * @param list the [List] of [T].
     * @param transformString a function that takes [T] and returns [String]
     * @param updateRequests a function that takes [List] of [T] and returns [Unit]
     */
    fun <T> filter(list : List<T>, transformString : (listItem : T) -> String, updateRequests : (newList: List<T>) -> Unit) : Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredList = mutableListOf<T>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList = list.toMutableList()
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in list) {
                        if (transformString(item).toLowerCase().contains(filterPattern)) {
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

    /***
     * Will generate a menu with a [SearchView].
     *
     * @param context the [Context].
     * @param resources the [Resources].
     * @param menu the [Menu].
     * @param inflater the [MenuInflater].
     * @param callback a function that takes [String] and returns [Unit].
     */
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