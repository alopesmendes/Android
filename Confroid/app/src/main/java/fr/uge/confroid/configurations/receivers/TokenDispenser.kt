package fr.uge.confroid.configurations.receivers

import android.app.Service
import android.content.Intent
import android.os.IBinder
import fr.uge.confroid.configurations.fragments.TokenActivity

/**
 * This service that opens a fragment with an external App name and its request id.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class TokenDispenser : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.extras?.getString("app")
        val request = intent?.getLongExtra("request", 0L)

        if (app != null) {
            Intent(this, TokenActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("app", app)
                putExtra("request", request)
                startActivity(this)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}