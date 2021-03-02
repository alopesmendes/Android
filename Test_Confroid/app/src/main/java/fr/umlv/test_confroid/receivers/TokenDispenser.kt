package fr.umlv.test_confroid.receivers

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.umlv.test_confroid.TokenActivity

class TokenDispenser : Service() {

    override fun onBind(intent: Intent): IBinder? {
        Log.i("token dispenser service", "bind")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = intent?.extras?.getString("app")

        if (app != null) {
            Intent(this, TokenActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("app", app)
                startActivity(this)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}