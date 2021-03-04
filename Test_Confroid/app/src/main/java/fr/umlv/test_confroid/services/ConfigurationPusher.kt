package fr.umlv.test_confroid.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import fr.umlv.test_confroid.Config
import fr.umlv.test_confroid.ConfigActivity
import fr.umlv.test_confroid.DatabaseHandler
import fr.umlv.test_confroid.MainActivity

class ConfigurationPusher : Service() {

    override fun onBind(intent: Intent): IBinder? {
        Log.i("pusher service", "bind")
        return null
    }

    //    AJOUTE LA CONFIG DANS LA BDD
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.extras?.getString("app")
        val version = intent?.extras?.getInt("version")
        val content = intent?.extras?.getSerializable("content")
        val tag = intent?.extras?.getString("tag")
        val request = intent?.getLongExtra("request", 0L)

        if (app != null && version != null && content != null && tag != null) {
            val config = MainActivity.model.addConfig(app, version, content.toString(), tag)

            if (config != null) {
                Intent(this, ConfigActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    putExtra("config", config)
                    putExtra("request", request)

                    startActivity(this)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}