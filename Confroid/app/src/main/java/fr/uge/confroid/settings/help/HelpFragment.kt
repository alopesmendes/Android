package fr.uge.confroid.settings.help

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import fr.uge.confroid.R
import fr.uge.confroid.settings.ZoomOutPageTransformer
import fr.uge.confroid.settings.help.fragments.*
import kotlinx.android.synthetic.main.fragment_help.*

/***
 * To help the User understand better how to use the application.
 * Uses the [HelpViewPageAdapter] that will allow the user to travel between the different fragments.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class HelpFragment : Fragment(R.layout.fragment_help) {

    private lateinit var navController: NavController
    private lateinit var adapter : HelpViewPageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        adapter = HelpViewPageAdapter(
            listOf(
                HelpMenuFragment(),
                HelpAppFragment(),
                HelpAllVersionsFragment(),
                HelpConfigFragment(),
                HelpSettingFragment(),
                HelpLoginFragment(),
                HelpRegisterFragment(),
                HelpConnectedFragment(),
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