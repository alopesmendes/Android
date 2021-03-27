package fr.uge.confroid.configurations.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import fr.uge.confroid.configurations.fragments.AppFragment
import fr.uge.confroid.configurations.fragments.ConfigFragment

/**
 * This service retrieves a config from an external App request,
 * and adds it in the database.
 * It sends the config to the ConfigFragment when successfully added.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class ConfigurationPusher : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.extras?.getString("app")
        val version = intent?.extras?.getInt("version")
        val content = intent?.extras?.getSerializable("content")
        val tag = intent?.extras?.getString("tag")
        val request = intent?.getLongExtra("request", 0L)

        if (app != null && version != null && content != null && tag != null) {
            val config = AppFragment.model.addConfig(app, version, content.toString(), tag)

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