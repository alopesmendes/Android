package fr.uge.confroid.configurations.fragments

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uge.confroid.R
import fr.uge.confroid.configurations.database.Model
import fr.uge.confroid.configurations.model.Application
import fr.uge.confroid.configurations.model.ApplicationAdapter
import fr.uge.confroid.configurations.receivers.TokenDispenser
import fr.uge.confroid.storageprovider.MyProvider
import fr.uge.confroid.utils.FilterUtils
import fr.uge.confroid.utils.RecyclerViewUtils
import fr.uge.confroid.web.WebSharedPreferences
import kotlinx.android.synthetic.main.fragment_app.*

/**
 * This Fragment displays all apps saved by Confroid as a RecyclerView.
 * The user can choose one and then Confroid opens a Fragment with all versions.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class AppFragment : Fragment(R.layout.fragment_app), ApplicationAdapter.OnItemClickListener {

    val filter: IntentFilter = IntentFilter()

    companion object {
        lateinit var model: Model
        lateinit var appAdapter: ApplicationAdapter
        lateinit var appLst: ArrayList<Application>
        const val broadcastConfigAction = "getConfig"
        const val broadcastAllVersionsAction = "getAllVersions"

        fun initAppList(): ArrayList<Application> {
            val res = ArrayList<Application>()
            var configCount: Int
            for (app in model.showTables()) {
                configCount = (model.getAllVersions(app))?.size ?: 0
                res.add(Application(app, configCount))
            }
            return res
        }
    }

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        navController = Navigation.findNavController(view)
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)

        //////////////

        filter.addAction(broadcastConfigAction)
        filter.addAction(broadcastAllVersionsAction)

        /////////////////////////////////////////////////////////

        model = Model(requireActivity())
        model.open()

        ////////////////////////////////////////////////
        initAppRecyclerView()

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
        when (requireActivity().intent.action) {
            Intent.ACTION_SEND -> {
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
                    } else {
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

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        FilterUtils.onCreateOptionsMenu(requireContext(), resources, menu, inflater) {
            appAdapter.filter.filter(it)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initAppRecyclerView() {
        appLst = initAppList()
        appAdapter = ApplicationAdapter(appLst, this@AppFragment)
        appRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AppFragment.context)
            adapter = appAdapter
        }

        RecyclerViewUtils.visibility(appLst.isEmpty(), appRecyclerView, imageLogoAppFragment)
    }

    override fun onItemClick(position: Int) {
        val clickedItem = appLst[position]
        val app = clickedItem.name
        val bundle = bundleOf("app" to app)
        navController.navigate(R.id.action_appFragment_to_allVersionsFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        initAppRecyclerView()
    }
}