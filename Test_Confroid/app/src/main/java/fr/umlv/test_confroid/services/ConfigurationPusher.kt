package fr.umlv.test_confroid.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import fr.umlv.test_confroid.Config
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
        Log.i("pusher service", "start")
        val app = intent?.extras?.getString("app")
        val version = intent?.extras?.getInt("version")
        val content = intent?.extras?.getSerializable("content")
        val tag = intent?.extras?.getString("tag")

        if (app != null && version != null && content != null && tag != null) {
            MainActivity.model.addConfig(app, version, content.toString(), tag)
        }

        return super.onStartCommand(intent, flags, startId)
    }
}