package fr.uge.confroid.configurations.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import fr.uge.confroid.configurations.fragments.AppFragment
import fr.uge.confroid.configurations.fragments.ConfigFragment

/**
 * This service retrieves a config from an external App request.
 * It sends the config to the ConfigFragment when successfully retrieved.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class ConfigurationPuller : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.extras?.getString("app")
        val version = intent?.extras?.getInt("version")
        val request = intent?.getLongExtra("request", 0L)

        if (app != null && version != null) {
            val config = AppFragment.model.getConfig(app, version)

            if (config != null) {
                val broadcastIntent = Intent()
                broadcastIntent.action = ConfigFragment.broadcastAction
                broadcastIntent.putExtra("config", config)
                broadcastIntent.putExtra("request", request)
                sendBroadcast(broadcastIntent)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}