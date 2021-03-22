package fr.uge.confroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import fr.uge.confroid.configurations.AppFragment
import fr.uge.confroid.settings.SettingFragment
import fr.uge.confroid.settings.SettingViewModel
import fr.uge.confroid.web.*
import fr.uge.confroid.worker.UploadWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, NavController.OnDestinationChangedListener  {

    private val settingViewModel: SettingViewModel by viewModels()
    private val loginViewModel : LoginViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration : AppBarConfiguration


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        localeSaved(resources, this)
        resetMenu()

        navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment))
        appBarConfiguration = AppBarConfiguration(navController.graph, mainDrawerLayout)

        configureToolBar()
        configureDrawerLayout()
        configureNavigationView()

        loginViewModel.selectedItem.observe(this, {
            resetMenu()
        })

        settingViewModel.selectedItem.observe(this, {
            val conf = resources.configuration
            conf.setLocale(Locale(it.toLowerCase()))
            resources.updateConfiguration(conf, resources.displayMetrics)
            resetMenu()
        })

        SettingFragment.enableMode(this)

        logAction()



        if (savedInstanceState == null) {
            navController.navigate(R.id.appFragment)
            mainNavigationView.setCheckedItem(R.id.homeItem)
        }

    }

    private fun resetMenu() {
        mainNavigationView.menu.clear()
        mainNavigationView.inflateMenu(R.menu.nav_menu)
        isLoggedInVisibility()
    }

    private fun configureToolBar() {
        setSupportActionBar(mainToolbar)
    }

    private fun configureDrawerLayout() {
        /*
        val toggle = ActionBarDrawerToggle(this, mainDrawerLayout, mainToolbar, R.string.navigation_open_drawer, R.string.navigation_close_drawer)
        mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        */
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(this)
        mainNavigationView.setupWithNavController(navController)
    }

    private fun configureNavigationView() {
        mainNavigationView.setNavigationItemSelectedListener(this)
    }


    private fun logAction() {
        if (WebSharedPreferences.getInstance(this).isLoggedIn()) {

            val user = WebSharedPreferences.getInstance(this).getUser()
            LoginRequest.request(this, user.username, user.password) {}
            work()

            Log.i("shared user", user.toString())

        }
        isLoggedInVisibility()
    }

    private fun isLoggedInVisibility() {
        if (WebSharedPreferences.getInstance(applicationContext).isLoggedIn()) {
            mainNavigationView.menu.findItem(R.id.logoutItem).isVisible = true
            mainNavigationView.menu.findItem(R.id.filesItem).isVisible = true
            mainNavigationView.menu.findItem(R.id.loginItem).isVisible = false
        } else {
            mainNavigationView.menu.findItem(R.id.logoutItem).isVisible = false
            mainNavigationView.menu.findItem(R.id.filesItem).isVisible = false
            mainNavigationView.menu.findItem(R.id.loginItem).isVisible = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.homeItem -> {
                navController.navigate(R.id.action_appFragment_self)
            }
            R.id.filesItem -> {
                navController.navigate(R.id.action_appFragment_to_filesFragment)
            }
            R.id.loginItem -> {
                navController.navigate(R.id.action_appFragment_to_loginFragment)
            }
            R.id.logoutItem -> {
                WebSharedPreferences.getInstance(applicationContext).logout()
                resetMenu()
            }
            R.id.licenseItem -> {
                navController.navigate(R.id.action_appFragment_to_licenseFragment)
            }
            R.id.helpItem -> {
                navController.navigate(R.id.action_appFragment_to_helpFragment)
            }

            R.id.settingItem -> {
                navController.navigate(R.id.action_appFragment_to_settingFragment)
            }
        }
        mainDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun work() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val work = PeriodicWorkRequestBuilder<UploadWorker>(20, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(work)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(this)
        localeSaved(resources, this)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(this)

    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun localeSaved(resources : Resources, context: Context) {
            val appSettingPrefs: SharedPreferences = context.getSharedPreferences(SettingFragment.PREFS, 0)
            val languageNames = resources.getStringArray(R.array.languages_list)
            val pos = appSettingPrefs.getInt(SettingFragment.LANG, 0)
            val lang = languageNames[pos]
            val conf = resources.configuration
            conf.setLocale(Locale(lang.toLowerCase()))
            resources.updateConfiguration(conf, resources.displayMetrics)

        }
    }
}