package fr.uge.confroid.configurations.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import fr.uge.confroid.R
import fr.uge.confroid.configurations.model.Config
import fr.uge.confroid.configurations.model.Field
import fr.uge.confroid.configurations.services.ConfigurationVersions
import kotlinx.android.synthetic.main.fragment_add_config.*


/**
 * This Fragment retrieves a config with a field selected by the user,
 * then the user can modify the field.
 * It will add a new config into the database.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class AddConfigFragment : Fragment(R.layout.fragment_add_config) {
    private lateinit var navController: NavController
    private lateinit var fields: ArrayList<Field>
    private var newversion: Int = -1

    lateinit var configsVersions: List<Int>
    val filter: IntentFilter = IntentFilter()

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                if (intent.action == AllVersionsFragment.broadcastAction) {
                    val versions = intent.getSerializableExtra("versions")
                    configsVersions = ((versions as Array<Config>).toList()).map {
                        it.version
                    }

                    Intent(activity, ConfigurationVersions::class.java).apply {
                        requireActivity().stopService(this)
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        var app = ""
        var oldversion = 0
        filter.addAction(AllVersionsFragment.broadcastAction)

        arguments?.let {
            fields = it.get("fields") as ArrayList<Field>
            app = it.getString("app_name").toString()
            oldversion = it.getInt("version_number")
        }
        app_name.text = app
        editTag.hint = "Version $oldversion modified"


        Intent(activity, ConfigurationVersions::class.java).apply {
            putExtra("app", app)
            requireActivity().startService(this)
        }

        finalizeConfig.setOnClickListener {
            if (editVersion.text.isNotEmpty()) {
                newversion = Integer.parseInt(editVersion.text.toString())
                if (configsVersions.contains(newversion)) {
                    Toast.makeText(activity, "version already saved", Toast.LENGTH_SHORT).show()
                } else {
                    val newTag: String = if (editTag.text.isEmpty()) {
                        "Version $oldversion modified"
                    } else {
                        editTag.text.toString()
                    }
                    AppFragment.model.addConfig(
                        app,
                        newversion,
                        toJsonFormat(),
                        newTag
                    )
                    navController.navigate(R.id.action_createConfigFragment_to_appFragment)
                }
            }
        }
    }

    private fun toJsonFormat(): String {
        val res: StringBuilder = java.lang.StringBuilder()
        res.append("{")
        for (i in fields.indices) {
            res.append(fields[i].toJsonFormat())
            if (i < fields.size - 1) {
                res.append(",")
            }
        }
        res.append("}")
        return res.toString()
    }

    //    POUR ENREGISTRER LE RECEIVER DE L'INTENT DU SERVICE DE PULLER
    override fun onPause() {
        activity?.unregisterReceiver(receiver)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(receiver, filter)
    }
}