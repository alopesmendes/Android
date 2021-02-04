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
import fr.umlv.test_confroid.test.reflect.*
import fr.umlv.test_confroid.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val filter: IntentFilter = IntentFilter()

    var configToSend: Config? = null
    var versionsToSend: Array<Config>? = null

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
                val content = intent.getSerializableExtra("config")
                test1.text = content.toString()
                configToSend = content as Config?
            } else if (intent.action == broadcastAllVersionsAction) {
                val versions = intent.getSerializableExtra("versions")
                test1.text = (versions as Array<*>).joinToString("\n", "{", "}")
                versionsToSend = versions as Array<Config>?
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

        /////////////////////////////////////////////////////////

        val prefs = ShoppingPreferences(mutableMapOf())
        val address1 = ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France")
        val address2 =
            ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country")
        val billingDetail = BillingDetail(
            "Bugdroid",
            "123456789",
            12,
            2021,
            123
        )
        prefs.shoppingInfos["home"] = ShoppingInfo(address1, billingDetail, true)
        prefs.shoppingInfos["work"] = ShoppingInfo(address2, billingDetail, false)

        val fields = ConfroidUtils.deepGetFields(mutableMapOf(), prefs)
        val content = ConfroidUtils.convertToBundle(fields)

        reflect_button.setOnClickListener {
            test1.text = fields.toString()
        }

        convert_to_bundle_button.setOnClickListener {
            test1.text = content.toString()
        }

        /////////////////////////////////////////////////////////

        model = Model(this)
        model.open()

        when {
            intent.action == Intent.ACTION_SEND -> {
                val request = intent.getIntExtra("request", -1)

                when (request) {
                    0 -> {
                        val app = intent.getStringExtra("app")
                        val version = intent.getIntExtra("version", 1)
                        val content = intent.getSerializableExtra("content")
                        val tag = intent.getStringExtra("tag")

                        Intent(this, ConfigurationPusher::class.java).apply {
                            putExtra("app", app)
                            putExtra("version", version)
                            putExtra("content", content)
                            putExtra("tag", tag)
                            startService(this)
                        }
                    }
                    1 -> {
                        val app = intent.getStringExtra("app")
                        val version = intent.getIntExtra("version", 1)

                        Intent(this, ConfigurationPuller::class.java).apply {
                            putExtra("app", app)
                            putExtra("version", version)
                            startService(this)
                        }
                    }
                    2 -> {
                        val app = intent.getStringExtra("app")

                        Intent(this, ConfigurationVersions::class.java).apply {
                            putExtra("app", app)
                            startService(this)
                        }
                    }
                }
            }
        }

        /////////////////////////////////////////////////////

//        ENVOIE LES DONNEES AU SERVICE DE PUSHER POUR STOCKER UNE CONFIG
        insert_button.setOnClickListener {
            Log.i("main", "pusher button")
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            val version = version_editText.text.toString()

            if (app.isBlank() || version.isBlank()) {
                Toast.makeText(this, "app and version required", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "configuration added", Toast.LENGTH_SHORT).show()
                ConfroidUtils.saveConfiguration(this, app, prefs, version)
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
                ConfroidUtils.loadConfiguration<Config>(this, app, version, null)
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
                ConfroidUtils.getConfigurationVersions(this, app, null)
            }
        }

//        ENVOIE LA CONFIG DEMANDEE A L'APP
        send_to_app_button.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND

                putExtra("content", configToSend.toString())

                startActivity(this)
            }
        }

//        ENVOIE TOUTES LES VERSIONS DES CONFIGS A L'APP
        send_all_to_app_button.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND

                putExtra("content", versionsToSend?.joinToString("\n", "{", "}"))

                startActivity(this)
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