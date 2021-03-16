package fr.uge.confroid.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment : Fragment(R.layout.fragment_help) {

    private lateinit var navController: NavController
    private lateinit var adapter : HelpPageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // temporaire à modifier en rajoutant des images
        adapter = HelpPageAdapter(
            listOf(
                HelpItem(resources.getString(R.string.tutorial), R.drawable.help_1, "Beginning of Tutorial"),
                HelpItem(resources.getString(R.string.settings), R.drawable.help_2, "The Menu")
            )
        )

        helpViewPager2.adapter = adapter
        helpViewPager2.setPageTransformer(ZoomOutPageTransformer())

        TabLayoutMediator(helpTabLayout, helpViewPager2) { tab, pos ->
        }.attach()
    }

}