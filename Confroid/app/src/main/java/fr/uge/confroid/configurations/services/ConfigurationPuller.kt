package fr.uge.confroid.configurations.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.uge.confroid.configurations.AppFragment
import fr.uge.confroid.configurations.ConfigActivity

class ConfigurationPuller : Service() {

    override fun onBind(intent: Intent): IBinder? {
        Log.i("puller service", "bind")
        return null
    }

    //    RECUPERE LA CONFIG, SI ELLE EXISTE, DEPUIS LA BDD
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.extras?.getString("app")
        val version = intent?.extras?.getInt("version")
        val request = intent?.getLongExtra("request", 0L)

        if (app != null && version != null) {
            val config = AppFragment.model.getConfig(app, version)

//            ENVOIE LA CONFIG AU RECEIVER DU MAINACTIVITY VIA UNE INTENT
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