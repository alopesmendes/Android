package fr.uge.confroid.configurations

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import fr.uge.confroid.R
import fr.uge.confroid.configurations.receivers.TokenDispenser
import fr.uge.confroid.configurations.services.ConfigurationPuller
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.web.WebSharedPreferences
import kotlinx.android.synthetic.main.fragment_app.*

class AppFragment : Fragment(R.layout.fragment_app) {

    val filter: IntentFilter = IntentFilter()

    var configToSend: Config? = null
    var versionsToSend: Array<Config>? = null

    companion object {
        lateinit var model: Model
        const val broadcastConfigAction = "getConfig"
        const val broadcastAllVersionsAction = "getAllVersions"
    }

    private lateinit var navController: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)

        //////////////

        filter.addAction(broadcastConfigAction)
        filter.addAction(broadcastAllVersionsAction)

        test1.movementMethod = ScrollingMovementMethod()

        /////////////////////////////////////////////////////////

        model = Model(requireActivity())
        model.open()

        if (WebSharedPreferences.getInstance(requireActivity()).isLoggedIn()) {
            val configs = model.getAllConfigs()
            for (c in configs) {
                MyProvider.writeFile(
                    requireActivity(),
                    "${c.app}_${c.version}.txt",
                    c.content.toByteArray()
                )
            }
        }
        when {
            requireActivity().intent.action == Intent.ACTION_SEND -> {
                val request = requireActivity().intent.getLongExtra("request", 0L)

                val app = requireActivity().intent.getStringExtra("app")
                val version = requireActivity().intent.getIntExtra("version", -1)
                val content = requireActivity().intent.getSerializableExtra("content")
                val tag = requireActivity().intent.getStringExtra("tag")
                val token = requireActivity().intent.getStringExtra("token")
                val receiver = requireActivity().intent.getStringExtra("receiver")

                if (prefs.getString(app, "") == token) {
                    if (receiver != null) {
                        when (receiver) {
                            "fr.uge.confroid.configurations.services.ConfigurationVersions" -> {
                                val bundle = bundleOf("app" to app, "request" to request)
                                navController.navigate(
                                    R.id.action_appFragment_to_allVersionsFragment,
                                    bundle
                                )
                            }
                            "fr.uge.confroid.configurations.services.ConfigurationPuller" -> {
                                val bundle = bundleOf(
                                    "app" to app,
                                    "version" to version,
                                    "request" to request
                                )
                                navController.navigate(
                                    R.id.action_appFragment_to_configFragment,
                                    bundle
                                )
                            }
                            "fr.uge.confroid.configurations.services.ConfigurationPusher" -> {
                                val bundle = bundleOf(
                                    "app" to app,
                                    "version" to version,
                                    "content" to content,
                                    "tag" to tag,
                                    "request" to request
                                )
                                navController.navigate(
                                    R.id.action_appFragment_to_configFragment,
                                    bundle
                                )
                            }
                        }
                    }
                    else {
                        if (app != null) {
                            model.deleteApp(app)
                        }
                    }
//                    TOKEN RETRIEVING
                } else {
                    Intent(activity, TokenDispenser::class.java).apply {
                        putExtra("app", app)
                        putExtra("request", request)
                        requireActivity().startService(this)
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
                MyProvider.writeFile(
                    requireActivity(),
                    "${c.app}_${c.version}.txt",
                    c.content.toByteArray()
                )
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
                    requireActivity().startService(this)
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
                val bundle = bundleOf("app" to app)
                navController.navigate(R.id.action_appFragment_to_allVersionsFragment, bundle)
            }
        }

        drop_table_button.setOnClickListener {
            val app = app_editText.text.toString().replace("\\s+".toRegex(), "")
            if (app.isBlank()) {
                Toast.makeText(activity, "configuration app required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                model.deleteApp(app)
            }
        }
    }

}