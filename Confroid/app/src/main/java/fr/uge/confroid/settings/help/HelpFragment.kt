package fr.uge.confroid.settings.help

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import fr.uge.confroid.R
import fr.uge.confroid.settings.ZoomOutPageTransformer
import fr.uge.confroid.settings.help.fragments.HelpLoginFragment
import fr.uge.confroid.settings.help.fragments.HelpMenuFragment
import fr.uge.confroid.settings.help.fragments.HelpRegisterFragment
import fr.uge.confroid.settings.help.fragments.HelpSettingFragment
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment : Fragment(R.layout.fragment_help) {

    private lateinit var navController: NavController
    private lateinit var adapter : HelpViewPageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        adapter = HelpViewPageAdapter(
            listOf(
                HelpMenuFragment(),
                HelpSettingFragment(),
                HelpLoginFragment(),
                HelpRegisterFragment(),
            ),
            childFragmentManager,
            lifecycle
        )
        helpViewPager2.adapter = adapter
        helpViewPager2.setPageTransformer(ZoomOutPageTransformer())

        TabLayoutMediator(helpTabLayout, helpViewPager2) { tab, pos ->
        }.attach()
    }

}