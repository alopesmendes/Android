package fr.uge.confroid.configurations.receivers

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.uge.confroid.configurations.TokenActivity

class TokenDispenser : Service() {

    override fun onBind(intent: Intent): IBinder? {
        Log.i("token dispenser service", "bind")
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