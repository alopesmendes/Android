package fr.uge.confroid.configurations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uge.confroid.R
import fr.uge.confroid.configurations.services.ConfigurationVersions
import kotlinx.android.synthetic.main.fragment_all_versions.*

/**
 * A simple [Fragment] subclass.
 * Use the [AllVersionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllVersionsFragment : Fragment() {

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

                        send_all_to_app_button.setOnClickListener {
                            Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra("content", configs?.joinToString("\n", "{", "}"))
                                putExtra("request", request)
                                startActivity(this)
                            }
                        }
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_versions, container, false)
    }

    fun onClickListener(position: Int) {
        val intent = Intent(activity, ConfigActivity::class.java)
        intent.putExtra("config", configs?.get(position))
        startActivity(intent)

//        val bundle = bundleOf("config" to configs?.get(position))
//        navController.navigate(R.id.action_allVersionsFragment_to_configFragment, bundle)
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