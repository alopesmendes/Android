package fr.uge.confroid.configurations.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.uge.confroid.configurations.AllVersionsFragment
import fr.uge.confroid.configurations.AppFragment

class ConfigurationVersions : Service() {

    override fun onBind(intent: Intent): IBinder? {
        Log.i("versions service", "bind")
        return null
    }

    //    RECUPERE LES VERSIONS D'UNE APP, SI ELLE EXISTE, DEPUIS LA BDD
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.getStringExtra("app")
        val request = intent?.getLongExtra("request", 0L)

        if (app != null) {
            val versions = AppFragment.model.getAllVersions(app)

//            ENVOIE LES VERSIONS AU RECEIVER DU MAINACTIVITY VIA UNE INTENT
            if (versions != null) {
                val broadcastIntent = Intent()
                broadcastIntent.action = AllVersionsFragment.broadcastAction
                broadcastIntent.putExtra("app", app)
                broadcastIntent.putExtra("versions", versions.toTypedArray())
                broadcastIntent.putExtra("request", request)
                sendBroadcast(broadcastIntent)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}