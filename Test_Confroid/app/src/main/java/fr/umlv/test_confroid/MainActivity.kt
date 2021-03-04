package fr.umlv.test_confroid

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_confroid.receivers.TokenDispenser
import fr.umlv.test_confroid.services.ConfigurationPuller
import fr.umlv.test_confroid.services.ConfigurationVersions
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        //////////////

        filter.addAction(broadcastConfigAction)
        filter.addAction(broadcastAllVersionsAction)

        test1.movementMethod = ScrollingMovementMethod()

        /////////////////////////////////////////////////////////

        model = Model(this)
        model.open()

        when {
            intent.action == Intent.ACTION_SEND -> {
                val request = intent.getLongExtra("request", 0L)

                val app = intent.getStringExtra("app")
                val version = intent.getIntExtra("version", -1)
                val content = intent.getSerializableExtra("content")
                val tag = intent.getStringExtra("tag")
                val token = intent.getStringExtra("token")
                val receiver = intent.getStringExtra("receiver")

                if (prefs.getString(app, "") == token) {
                    if (receiver != null) {
                        Intent(this, Class.forName(receiver)).apply {
                            putExtra("app", app)
                            putExtra("version", version)
                            putExtra("content", content)
                            putExtra("tag", tag)
                            putExtra("request", request)
                            startService(this)
                        }
                    }
//                    TOKEN RETRIEVING
                } else {
                    Intent(this, TokenDispenser::class.java).apply {
                        putExtra("app", app)
                        putExtra("request", request)
                        startService(this)
                    }
                }
            }
        }

        /////////////////////////////////////////////////////

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
            test1.text = configs.joinToString("\n\n", "{", "}")
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
                    putExtra("version", Integer.parseInt(version))
                    startService(this)
                }
            }
        }

        show_token.setOnClickListener {
            val app = prefs.getString(app_editText.text.toString(), "")
            Toast.makeText(this, app, Toast.LENGTH_SHORT).show()
        }

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
}