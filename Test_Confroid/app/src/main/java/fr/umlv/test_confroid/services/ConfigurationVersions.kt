package fr.umlv.test_confroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.umlv.test_confroid.AllVersionsActivity
import fr.umlv.test_confroid.MainActivity

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
            val versions = MainActivity.model.getAllVersions(app)

//            ENVOIE LES VERSIONS AU RECEIVER DU MAINACTIVITY VIA UNE INTENT
            if (versions != null) {
                Intent(this, AllVersionsActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    putExtra("app", app)
                    putExtra("versions", versions.toTypedArray())
                    putExtra("request", request)

                    startActivity(this)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}