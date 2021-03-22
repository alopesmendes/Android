package fr.uge.confroid.configurations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uge.confroid.R
import fr.uge.confroid.configurations.services.ConfigurationVersions
import kotlinx.android.synthetic.main.fragment_add_config.*
import kotlinx.android.synthetic.main.fragment_all_versions.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddConfigFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddConfigFragment : Fragment(R.layout.fragment_add_config) {
    private lateinit var navController: NavController
    lateinit var fields: ArrayList<Field>
    var newversion: Int = -1

    lateinit var configsVersions: List<Int>
    val filter: IntentFilter = IntentFilter()

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
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
        var app: String = ""
        var oldversion: Int = 0
        filter.addAction(AllVersionsFragment.broadcastAction)

        arguments?.let {
            fields = it.get("fields") as ArrayList<Field>
            app = it.getString("app_name").toString()
            oldversion = it.getInt("version_number")
        }
        app_name.text = app
        newContentJsonFormat.text = toJsonFormat()
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
                    AppFragment.model.addConfig(
                        app,
                        newversion,
                        toJsonFormat(),
                        editTag.text.toString()
                    )
                    navController.navigate(R.id.action_createConfigFragment_to_appFragment)
                }
            }
        }
    }

    private fun toJsonFormat(): String {
        var res: StringBuilder = java.lang.StringBuilder()
        res.append("{")
        for (i in fields.indices) {
            Log.i("field a insérer", fields[i].toString())
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