package fr.uge.confroid.configurations

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import fr.uge.confroid.R
import fr.uge.confroid.configurations.receivers.TokenDispenser
import fr.uge.confroid.configurations.services.ConfigurationPuller
import fr.uge.confroid.configurations.services.ConfigurationVersions
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.web.WebSharedPreferences
import kotlinx.android.synthetic.main.fragment_app.*

class AppFragment : Fragment(R.layout.fragment_app) {

    val filter: IntentFilter = IntentFilter()

    var configToSend: Config? = null
    var versionsToSend: Array<Config>? = null

    companion object {
        lateinit var model: Model
        val broadcastConfigAction = "getConfig"
        val broadcastAllVersionsAction = "getAllVersions"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity!!.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        //////////////

        filter.addAction(broadcastConfigAction)
        filter.addAction(broadcastAllVersionsAction)

        test1.movementMethod = ScrollingMovementMethod()

        /////////////////////////////////////////////////////////

        model = Model(activity!!)
        model.open()

        if (WebSharedPreferences.getInstance(activity!!).isLoggedIn()) {
            val configs = model.getAllConfigs()
            for (c in configs) {
                MyProvider.writeFile(activity!!, "${c.app}_${c.version}.txt", c.content.toByteArray())
            }
        }
        when {
            activity!!.intent.action == Intent.ACTION_SEND -> {
                val request = activity!!.intent.getLongExtra("request", 0L)

                val app = activity!!.intent.getStringExtra("app")
                val version = activity!!.intent.getIntExtra("version", -1)
                val content = activity!!.intent.getSerializableExtra("content")
                val tag = activity!!.intent.getStringExtra("tag")
                val token = activity!!.intent.getStringExtra("token")
                val receiver = activity!!.intent.getStringExtra("receiver")

                if (prefs.getString(app, "") == token) {
                    if (receiver != null) {
                        Intent(activity, Class.forName(receiver)).apply {
                            putExtra("app", app)
                            putExtra("version", version)
                            putExtra("content", content)
                            putExtra("tag", tag)
                            putExtra("request", request)
                            activity!!.startService(this)
                        }
                    }
//                    TOKEN RETRIEVING
                } else {
                    Intent(activity, TokenDispenser::class.java).apply {
                        putExtra("app", app)
                        putExtra("request", request)
                        activity!!.startService(this)
                    }
                }
            }
        }

        /////////////////////////////////////////////////////

        delete_button.setOnClickListener {
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            val id = id_editText.text.toString()
            if (app.isBlank() || id.isBlank()) {
                Toast.makeText(activity, "configuration app and id required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(activity, "configuration deleted", Toast.LENGTH_SHORT).show()
                model.deleteConfig(app, id.toInt())
            }
        }

        select_all_button.setOnClickListener {
            val configs = model.getAllConfigs()
            test1.text = configs.joinToString("\n\n", "{", "}")
            for (c in configs) {
                MyProvider.writeFile(activity!!, "${c.app}_${c.version}.txt", c.content.toByteArray())
            }

            


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
                Toast.makeText(
                    activity,
                    "configuration app and version required",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(activity, "configuration selected", Toast.LENGTH_SHORT).show()
                Intent(activity, ConfigurationPuller::class.java).apply {
                    putExtra("app", app)
                    putExtra("version", Integer.parseInt(version))
                    activity!!.startService(this)
                }
            }
        }

        show_token.setOnClickListener {
            val app = prefs.getString(app_editText.text.toString(), "")
            Toast.makeText(activity, app, Toast.LENGTH_SHORT).show()
        }

        all_versions_button.setOnClickListener {
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            if (app.isBlank()) {
                Toast.makeText(activity, "configuration app required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(activity, "all versions selected", Toast.LENGTH_SHORT).show()
                Intent(activity, ConfigurationVersions::class.java).apply {
                    putExtra("app", app)
                    activity!!.startService(this)
                }

            }
        }
    }

}