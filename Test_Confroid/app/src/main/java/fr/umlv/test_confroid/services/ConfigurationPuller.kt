package fr.umlv.test_confroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.umlv.test_confroid.ConfigActivity
import fr.umlv.test_confroid.MainActivity

class ConfigurationPuller : Service() {

    override fun onBind(intent: Intent): IBinder? {
        Log.i("puller service", "bind")
        return null
    }

    //    RECUPERE LA CONFIG, SI ELLE EXISTE, DEPUIS LA BDD
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("puller service", "start")
        val app = intent?.extras?.getString("app")
        val version = intent?.extras?.getInt("version")

        if (app != null && version != null) {
            val config = MainActivity.model.getConfig(app, version)

//            ENVOIE LA CONFIG AU RECEIVER DU MAINACTIVITY VIA UNE INTENT
//            Intent().apply {
//                action = MainActivity.broadcastConfigAction
//
//                putExtra("config", config)
//
//                sendBroadcast(this)
//            }

            if (config != null) {
                Intent(this, ConfigActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    putExtra("config", config)

                    startActivity(this)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}