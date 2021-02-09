package fr.umlv.test_confroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.umlv.test_confroid.MainActivity

class ConfigurationVersions : Service() {

    override fun onBind(intent: Intent): IBinder? {
        Log.i("versions service", "bind")
        return null
    }

    //    RECUPERE LES VERSIONS D'UNE APP, SI ELLE EXISTE, DEPUIS LA BDD
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("versions service", "start")
        val app = intent?.extras?.getString("app")
        Log.i("versions service", app.toString())
        if (app != null) {
            val versions = MainActivity.model.getAllVersions(app)

//            ENVOIE LES VERSIONS AU RECEIVER DU MAINACTIVITY VIA UNE INTENT
            if (versions != null) {
                val broadcastIntent = Intent()
                broadcastIntent.action = "getAllVersions"
                broadcastIntent.putExtra("versions", versions.toTypedArray())
                sendBroadcast(broadcastIntent)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}