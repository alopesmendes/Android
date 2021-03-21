package fr.uge.confroid.configurations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.fragment_leaf.*

/**
 * A simple [Fragment] subclass.
 * Use the [LeafFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeafFragment : Fragment(R.layout.fragment_leaf) {

    lateinit var field:Field

    companion object {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            field = it.get("field") as Field
        }
        Log.i("in fragment", field.toString())
        Log.i("fieldName", field.name)
        FieldNameValue.text = field.name
        oldValueText.text = field.content

        button_modify.setOnClickListener{
            if (!field_editValue.text.isNullOrBlank()){
                field = Field(field.name, field_editValue.text.toString(), null)
                Log.i("nouvelle valeur", field.toString())
                navigateBack()
            }
        }
    }



    private fun navigateBack() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "updateField",
            field
        )
        findNavController().popBackStack()
    }


}