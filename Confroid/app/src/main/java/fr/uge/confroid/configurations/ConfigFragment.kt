package fr.uge.confroid.configurations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uge.confroid.R
import fr.uge.confroid.configurations.services.ConfigurationPuller
import fr.uge.confroid.configurations.services.ConfigurationPusher
import kotlinx.android.synthetic.main.fragment_all_versions.*
import kotlinx.android.synthetic.main.fragment_config.*

/**
 * A simple [Fragment] subclass.
 * Use the [ConfigFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfigFragment : Fragment(R.layout.fragment_config) {

    val filter: IntentFilter = IntentFilter()
    lateinit var adapter: FieldAdapter
    var positionToChange = -1
    var list : ArrayList<Field> = arrayListOf()

    companion object {
        val broadcastAction = "getOneVersion"
    }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                if (intent.action == broadcastAction) {
                    val config = intent.extras?.get("config") as Config
                    AppNameConfig.text = config.app
                    textVersionConfig.text = config.version.toString()
                    textTagConfig.text = config.tag

                    val content: String = config.content
                    var fields = toListField(toList(content))
                    if (list.isEmpty()) {
                        list = fields
                    }
                    adapter = FieldAdapter(this@ConfigFragment, list)
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
            Log.i("changement a faire", field.toString())
            list[positionToChange] = field
            adapter.updatevalue(positionToChange,field)
            adapter.notifyItemChanged(positionToChange)
            Log.i("changement",adapter.getfield(positionToChange).toString())
        })
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
            if (content.get(i) == '{') {
                nb_rec++
            } else if (content.get(i) == '}') {
                nb_rec--
            } else if (content.get(i) == ',' && (nb_rec == 0)) {
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
        var test = content.subSequence(1, content.length - 1).toString()
        var pair = firstField(test)
        var res = arrayListOf(pair.first)
        var bool = pair.second
        while (bool.isNotEmpty()) {
            pair = firstField(bool)
            res.add(pair.first)
            bool = pair.second
        }
        return res
    }

    private fun toListField(strs: List<String>): ArrayList<Field> {
        var lst = arrayListOf<Field>()
        for (str in strs) {
            var field = str.substring(0, str.indexOf("="))
            var content = str.substring(str.indexOf("=") + 1)
            var testList = toList(content)
            if (testList.size == 1) {
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

    fun onClickListener(position: Int){
        val field = adapter.getfield(position)
        if (field.content.isNullOrBlank()){

        }else{
            positionToChange = position
            navigateToLeaf(field)
        }
    }


}