package fr.uge.confroid.configurations.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uge.confroid.MainActivity
import fr.uge.confroid.R
import fr.uge.confroid.configurations.model.Config
import fr.uge.confroid.configurations.model.Field
import fr.uge.confroid.configurations.model.FieldAdapter
import fr.uge.confroid.configurations.services.ConfigurationPuller
import fr.uge.confroid.configurations.services.ConfigurationPusher
import kotlinx.android.synthetic.main.fragment_all_versions.*
import kotlinx.android.synthetic.main.fragment_config.*

/**
 * This Fragment displays a config content.
 * It displays all fields as a RecyclerView and the user
 * can navigate into them for a version update.
 */
class ConfigFragment : Fragment(R.layout.fragment_config) {

    val filter: IntentFilter = IntentFilter()
    lateinit var adapter: FieldAdapter
    private var positionToChange = -1
    var list: ArrayList<Field> = arrayListOf()

    companion object {
        const val broadcastAction = "getOneVersion"
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                if (intent.action == broadcastAction) {
                    val config = intent.extras?.get("config") as Config
                    AppNameConfig.text = config.app
                    textVersionConfig.text = config.version.toString()
                    textTagConfig.text = config.tag

                    val content: String = config.content
                    val fields = toListField(toList(content))
                    if (list.isEmpty()) {
                        list = fields
                    }
                    adapter = FieldAdapter(this@ConfigFragment, BranchFragment(), list, true)
                    RvValueField.adapter = adapter
                    RvValueField.layoutManager = LinearLayoutManager(activity)
                    RvValueField.setHasFixedSize(true)

                    val request = intent.getLongExtra("request", 0L)
                    if (request != 0L) {
                        send_to_app_button.visibility = View.VISIBLE
                        send_to_app_button.setOnClickListener {
                            Intent().apply {
                                action = Intent.ACTION_SEND

                                putExtra("content", config.toString())
                                putExtra("request", request)

                                startActivity(this)
                            }
                        }
                    }

                    delete_button.setOnClickListener {
                        AppFragment.model.deleteConfig(config.app, config.id)
                        Intent(activity, MainActivity::class.java).apply {
                            startActivity(this)
                        }
                    }

                    Intent(activity, ConfigurationPuller::class.java).apply {
                        requireActivity().stopService(this)
                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filter.addAction(broadcastAction)
        val app = arguments?.getString("app")
        val version = arguments?.getInt("version")
        val content = arguments?.getSerializable("content")
        val tag = arguments?.getString("tag")
        val request = arguments?.getLong("request")

        if (content != null) {
            Intent(activity, ConfigurationPusher::class.java).apply {
                putExtra("app", app)
                putExtra("version", version)
                putExtra("content", content)
                putExtra("tag", tag)
                putExtra("request", request)
                requireActivity().startService(this)
            }
        } else {
            Intent(activity, ConfigurationPuller::class.java).apply {
                putExtra("app", app)
                putExtra("version", version)
                putExtra("request", request)
                requireActivity().startService(this)
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Field>(
            "updateField"
        )?.observe(viewLifecycleOwner, { field ->
            //Log.i("changement a faire", field.toString())
            list[positionToChange] = field
            adapter.updatevalue(positionToChange, field)
            adapter.notifyItemChanged(positionToChange)
            //Log.i("changement",adapter.getfield(positionToChange).toString())
        })

        createConfig.setOnClickListener {
            val bundle = bundleOf("fields" to list, "app_name" to app, "version_number" to version)
            findNavController().navigate(R.id.action_configFragment_to_addConfigFragment, bundle)
        }
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

    private fun firstField(content: String): Pair<String, String> {
        var i = 0
        var nb_rec = 0
        while (i < content.length) {
            if (content[i] == '{') {
                nb_rec++
            } else if (content[i] == '}') {
                nb_rec--
            } else if (content[i] == ',' && (nb_rec == 0)) {
                return Pair(
                    content.subSequence(0, i).toString(),
                    content.subSequence(i + 1, content.length).toString()
                )
            }
            i++
        }
        return Pair(content, "")
    }

    private fun toList(content: String): List<String> {
        if (content.length < 2) {
            return arrayListOf()
        }
        val test = content.subSequence(1, content.length - 1).toString()
        var pair = firstField(test)
        val res = arrayListOf(pair.first)
        var bool = pair.second
        while (bool.isNotEmpty()) {
            pair = firstField(bool)
            res.add(pair.first)
            bool = pair.second
        }
        return res
    }

    private fun toListField(strs: List<String>): ArrayList<Field> {
        val lst = arrayListOf<Field>()
        for (str in strs) {
            val field = str.substring(0, str.indexOf("="))
            val content = str.substring(str.indexOf("=") + 1)
            val testList = toList(content)
            if (testList.size <= 1) {
                lst.add(Field(field, content, null))
            } else {
                lst.add(Field(field, null, toListField(testList)))
            }

        }
        return lst
    }

    private fun navigateToLeaf(field: Field) {
        Log.i("gotoLeaf", field.toString())
        val bundle = bundleOf("field" to field)
        findNavController().navigate(R.id.action_configFragment_to_leafFragment, bundle)
    }

    private fun navigateToBranch(field: Field) {
        Log.i("gotoLeaf", field.toString())
        val bundle = bundleOf("field" to field)
        findNavController().navigate(R.id.action_configFragment_to_branchFragment, bundle)
    }

    fun onClickListener(position: Int) {
        val field = adapter.getfield(position)
        positionToChange = position
        if (field.content.isNullOrBlank()) {
            navigateToBranch(field)
        } else {
            navigateToLeaf(field)
        }
    }


}