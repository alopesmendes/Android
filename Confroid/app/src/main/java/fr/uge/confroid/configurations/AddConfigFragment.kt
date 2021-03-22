package fr.uge.confroid.configurations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_add_config.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddConfigFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddConfigFragment : Fragment(R.layout.fragment_add_config) {
    lateinit var fields : ArrayList<Field>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            fields = it.get("fields") as ArrayList<Field>
        }

        newContent.text = fields.toString()
    }


    companion object {
    }
}