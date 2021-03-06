package fr.uge.confroid.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_setting.*

/***
 * Will allow the user to change the settings of Confroid.
 * Can change the theme and language of the application.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class SettingViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<String>()
    val selectedItem: LiveData<String> get() = mutableSelectedItem

    fun selectItem(item: String) {
        mutableSelectedItem.value = item
    }

}


class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val viewModel: SettingViewModel by activityViewModels()

    private lateinit var navController: NavController
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        createSpinnerLanguages();
        defineMode()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun defineMode() {
        val appSettingPrefs: SharedPreferences = requireActivity().getSharedPreferences(PREFS, 0)
        val sharedPreferencesEdit = appSettingPrefs.edit()
        val isNightMode = enableMode(requireActivity())

        if (isNightMode) {
            switchDarkMode.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_moon,
                    requireActivity().theme
                )
            )
            textDarkMode.text = resources.getString(R.string.dark_mode_text)
        } else {
            switchDarkMode.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_sun,
                    requireActivity().theme
                )
            )
            textDarkMode.text = resources.getString(R.string.light_mode_text)
        }

        switchDarkMode.setOnClickListener {
            if (!isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferencesEdit.putBoolean(NIGHT_MODE, true)
                sharedPreferencesEdit.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferencesEdit.putBoolean(NIGHT_MODE, false)
                sharedPreferencesEdit.apply()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createSpinnerLanguages() {
        val appSettingPrefs: SharedPreferences = requireActivity().getSharedPreferences(PREFS, 0)
        var pos = appSettingPrefs.getInt(LANG, 0)
        val languageNames = resources.getStringArray(R.array.languages_list)
        val flags = resources.obtainTypedArray(R.array.flags_list)
        languageNames[0] = languageNames[pos]

        val customDropDownAdapter = LanguageAdapter(requireActivity(), languageNames, flags)
        spinnerLanguages.adapter = customDropDownAdapter
        spinnerLanguages.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val lang = spinnerLanguages.adapter.getItem(position).toString()
                    val appSettingPrefs: SharedPreferences = requireActivity().getSharedPreferences(PREFS, 0)
                    val sharedPreferencesEdit = appSettingPrefs.edit()

                    sharedPreferencesEdit.putInt(LANG, position)
                    sharedPreferencesEdit.apply()
                    viewModel.selectItem(lang)
                    navController.navigate(R.id.action_settingFragment_self)
                    //Intent(requireContext(), MainActivity::class.java).apply { startActivity(this) }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    companion object {
        const val PREFS = "AppSettingPrefs"
        const val NIGHT_MODE = "NightMode"
        const val LANG = "LangSelected"

        /***
         * Enables the opposite theme.
         * @param context the [Context] to get the [SharedPreferences].
         * @return true if night mode is enable otherwise false.
         */
        fun enableMode(context: Context): Boolean {
            val appSettingPrefs: SharedPreferences = context.getSharedPreferences(PREFS, 0)
            val isNightModeOn = appSettingPrefs.getBoolean(NIGHT_MODE, false)

            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            return isNightModeOn
        }
    }


}