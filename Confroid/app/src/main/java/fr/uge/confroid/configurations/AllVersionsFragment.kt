package fr.uge.confroid.configurations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uge.confroid.R
import fr.uge.confroid.configurations.services.ConfigurationVersions
import fr.uge.confroid.utils.FilterUtils
import kotlinx.android.synthetic.main.fragment_all_versions.*

/**
 * A simple [Fragment] subclass.
 * Use the [AllVersionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllVersionsFragment : Fragment(R.layout.fragment_all_versions) {

    private lateinit var navController: NavController
    private var configs: List<Config>? = null
    private var configAdapter: ConfigAdapter? = null
    val filter: IntentFilter = IntentFilter()

    companion object {
        val broadcastAction = "getAllVersions"
    }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                if (intent.action == broadcastAction) {
                    val versions = intent.getSerializableExtra("versions")
                    configs = ((versions as Array<Config>).toList())
                    configAdapter = configs?.let { ConfigAdapter(this@AllVersionsFragment, it) }
                    RvAllVersions.adapter = configAdapter
                    RvAllVersions.layoutManager = LinearLayoutManager(activity)
                    RvAllVersions.setHasFixedSize(true)

                    val request = intent.getLongExtra("request", 0L)
                    if (request != 0L) {
                        send_all_to_app_button.visibility = View.VISIBLE
                    }

                    Intent(activity, ConfigurationVersions::class.java).apply {
                        requireActivity().stopService(this)
                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        navController = Navigation.findNavController(view)
        filter.addAction(broadcastAction)

        val app = arguments?.getString("app")
        val request = arguments?.getLong("request")
        AppName.text = app
        Intent(activity, ConfigurationVersions::class.java).apply {
            putExtra("app", app)
            putExtra("request", request)
            requireActivity().startService(this)
        }

        send_all_to_app_button.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra("content", configs?.joinToString("\n", "{", "}"))
                putExtra("request", request)
                startActivity(this)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        FilterUtils.onCreateOptionsMenu(requireContext(), resources, menu, inflater) {
            configAdapter?.filter?.filter(it)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    fun onClickListener(position: Int) {
        val config = configs?.get(position)
        val bundle =
            bundleOf("config" to config, "app" to config?.app, "version" to config?.version)
        navController.navigate(R.id.action_allVersionsFragment_to_configFragment, bundle)
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