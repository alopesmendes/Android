package fr.umlv.test_confroid;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import fr.umlv.test_confroid.services.ConfigurationPuller
import fr.umlv.test_confroid.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_all_versions.*
import kotlinx.android.synthetic.main.activity_main.*


class AllVersions : AppCompatActivity() {

    val filter: IntentFilter = IntentFilter()
    val broadcastAction = "getAllVersions"
    /*
    LORSQUE LE RECEIVER RECOIT LA CONFIG DU SERVICE PULLER,
    AFFICHE LA CONFIG DANS LA TEXTVIEW
    ET MET FIN AU SERVICE DE PULLER
    */
    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == broadcastAction) {
                Log.i("versions service", "receiver de AllVersions")
                val versions = intent.getSerializableExtra("versions")
                VersionsList.text = (versions as Array<*>).joinToString("\n", "{", "}")
                Intent(this@AllVersions, ConfigurationPuller::class.java).apply {
                    stopService(this)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_versions)
        filter.addAction(broadcastAction)
        val intent = intent
        if (intent != null) {
            var appName: String = ""
            if (intent.hasExtra("app")) {
                appName = intent.getStringExtra("app").toString()
                AppName.text = appName
                ConfroidUtils.getConfigurationVersions(this, appName, null)
            }
        }

    }

    //    POUR ENREGISTRER LE RECEIVER DE L'INTENT DU SERVICE DE PULLER
    override fun onPause() {
        unregisterReceiver(receiver)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, filter)
    }
}