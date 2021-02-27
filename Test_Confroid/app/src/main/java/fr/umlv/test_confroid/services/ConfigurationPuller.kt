package fr.umlv.test_confroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
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
            val broadcastIntent = Intent()
            broadcastIntent.action = MainActivity.broadcastConfigAction
            broadcastIntent.putExtra("config", config)
            sendBroadcast(broadcastIntent)
        }

        return super.onStartCommand(intent, flags, startId)
    }
}