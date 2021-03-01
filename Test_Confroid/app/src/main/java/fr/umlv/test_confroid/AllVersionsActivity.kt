package fr.umlv.test_confroid;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.umlv.test_confroid.services.ConfigurationPuller
import fr.umlv.test_confroid.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_all_versions.*


class AllVersionsActivity : AppCompatActivity() {

    val filter: IntentFilter = IntentFilter()
    val broadcastAction = "getAllVersions"
    private var configAdapter : ConfigAdapter?=null
    private var configs : List<Config>? = null
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
                configs = ((versions as Array<Config>).toList())
                configAdapter = configs?.let { ConfigAdapter(this@AllVersionsActivity, it) }
                RvAllVersions.adapter = configAdapter
                RvAllVersions.layoutManager = LinearLayoutManager(this@AllVersionsActivity)
                RvAllVersions.setHasFixedSize(true)
                Log.i("résultat", configs!!.size.toString())

                Intent(this@AllVersionsActivity, ConfigurationPuller::class.java).apply {
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

        send_all_to_app_button.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND

                putExtra("content", configs?.joinToString("\n", "{", "}"))

                startActivity(this)
            }
        }

    }

    fun onClickListener(position: Int){
        val intent = Intent(this, ConfigActivity::class.java)
        intent.putExtra("config", configs?.get(position))
        startActivity(intent)
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