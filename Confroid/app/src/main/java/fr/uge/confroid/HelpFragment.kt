package fr.uge.confroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.NavController
import androidx.navigation.Navigation

class HelpFragment : Fragment(R.layout.fragment_help) {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_help, container, false)
        // TODO: change URL
        val url = "https://raw.githubusercontent.com/alopesmendes/Android/main/LICENSE"
        val view = rootView.findViewById(R.id.webView) as WebView
        view.settings.javaScriptEnabled = true
        view.loadUrl(url)

        return rootView ;
    }
}