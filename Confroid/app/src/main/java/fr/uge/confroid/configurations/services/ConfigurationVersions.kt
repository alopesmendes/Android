package fr.uge.confroid.configurations.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import fr.uge.confroid.configurations.AllVersionsFragment
import fr.uge.confroid.configurations.AppFragment

/**
 * This service retrieves all versions of a config from an external App request.
 * It sends them to the AllVersionsFragment when successfully retrieved.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class ConfigurationVersions : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.getStringExtra("app")
        val request = intent?.getLongExtra("request", 0L)

        if (app != null) {
            val versions = AppFragment.model.getAllVersions(app)

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