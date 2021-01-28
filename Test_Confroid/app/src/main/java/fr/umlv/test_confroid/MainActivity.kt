package fr.umlv.test_confroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_confroid.services.ConfigurationPuller
import fr.umlv.test_confroid.services.ConfigurationPusher
import fr.umlv.test_confroid.services.ConfigurationVersions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val filter: IntentFilter = IntentFilter()

    companion object {
        lateinit var model: Model
        val broadcastConfigAction = "getConfig"
        val broadcastAllVersionsAction = "getAllVersions"
    }

    /*
    LORSQUE LE RECEIVER RECOIT LA CONFIG DU SERVICE PULLER,
    AFFICHE LA CONFIG DANS LA TEXTVIEW
    ET MET FIN AU SERVICE DE PULLER
    */
    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == broadcastConfigAction) {
                test1.text = intent.getSerializableExtra("config").toString()
            } else if (intent.action == broadcastAllVersionsAction) {
                val versions = intent.getSerializableExtra("versions") as Array<*>
                test1.text = versions.joinToString("\n", "{", "}")
            }
            Intent(this@MainActivity, ConfigurationPuller::class.java).apply {
                stopService(this)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filter.addAction(broadcastConfigAction)
        filter.addAction(broadcastAllVersionsAction)

        model = Model(this)
        model.open()

//        when {
//            intent.action == Intent.ACTION_SEND -> {

//            }
//        }

        /////////////////////////////////////////////////////

//        ENVOIE LES DONNEES AU SERVICE DE PUSHER POUR STOCKER UNE CONFIG
        insert_button.setOnClickListener {
            Log.i("main", "pusher button")
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            val version = version_editText.text.toString()
            val content = content_editText.text.toString()
            val tag = tag_editText.text.toString()

            if (app.isBlank() || version.isBlank() || content.isBlank()) {
                Toast.makeText(this, "app, version and content required", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "configuration added", Toast.LENGTH_SHORT).show()
                Intent(this, ConfigurationPusher::class.java).apply {
                    putExtra("app", app)
                    putExtra("version", version.toInt())
                    putExtra("content", content)
                    putExtra("tag", tag)

                    startService(this)
                }
            }
        }

        delete_button.setOnClickListener {
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            val id = id_editText.text.toString()
            if (app.isBlank() || id.isBlank()) {
                Toast.makeText(this, "configuration app and id required", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "configuration deleted", Toast.LENGTH_SHORT).show()
                model.deleteConfig(app, id.toInt())
            }
        }

        select_all_button.setOnClickListener {
            val configs = model.getAllConfigs()
            test1.text = configs.joinToString("\n", "{", "}")
        }

        reset_button.setOnClickListener {
            model.reset()
        }

        show_all_tables_button.setOnClickListener {
            test1.text = model.showTables().joinToString("\n", "{", "}")
        }

//        ENVOIE LES DONNEES AU SERVICE DE PULLER POUR DEMANDER UNE CONFIG
        select_one_button.setOnClickListener {
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            val version = version_editText.text.toString()
            if (app.isBlank() || version.isBlank()) {
                Toast.makeText(this, "configuration app and version required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "configuration selected", Toast.LENGTH_SHORT).show()
                Intent(this, ConfigurationPuller::class.java).apply {
                    putExtra("app", app)
                    putExtra("version", version.toInt())

                    startService(this)
                }
            }
        }

//        ENVOIE LES DONNEES AU SERVICE DE PULLER POUR DEMANDER TOUTES LES VERSIONS D'UNE APPLI
        all_versions_button.setOnClickListener {
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            if (app.isBlank()) {
                Toast.makeText(this, "configuration app required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "all versions selected", Toast.LENGTH_SHORT).show()
                Intent(this, ConfigurationVersions::class.java).apply {
                    putExtra("app", app)

                    startService(this)
                }
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