package fr.uge.confroid.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingViewModel : ViewModel() {

}


class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val viewModel: SettingViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appSettingPrefs: SharedPreferences = activity!!.getSharedPreferences(PREFS, 0)
        val sharedPreferencesEdit = appSettingPrefs.edit()
        val isNightMode = enableMode(activity!!)

        if (isNightMode) {
            switchDarkMode.setImageDrawable(resources.getDrawable(R.drawable.ic_moon, activity!!.theme))
            textDarkMode.text = resources.getString(R.string.dark_mode_text)
        } else {
            switchDarkMode.setImageDrawable(resources.getDrawable(R.drawable.ic_sun, activity!!.theme))
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

    companion object {
        const val PREFS = "AppSettingPrefs"
        const val NIGHT_MODE = "NightMode"

        fun enableMode(context: Context) : Boolean {
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