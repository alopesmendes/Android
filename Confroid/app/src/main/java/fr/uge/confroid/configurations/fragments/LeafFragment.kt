package fr.uge.confroid.configurations.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fr.uge.confroid.R
import fr.uge.confroid.configurations.model.Field
import kotlinx.android.synthetic.main.fragment_leaf.*

/**
 * This Fragment displays a field of a config with its old value
 * and the user can modify it.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class LeafFragment : Fragment(R.layout.fragment_leaf) {

    private lateinit var field: Field

    companion object {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            field = it.get("field") as Field
        }
        FieldNameValue.text = field.name
        oldValueText.text = field.content

        button_modify.setOnClickListener {
            if (!field_editValue.text.isNullOrBlank()) {
                field = Field(field.name, field_editValue.text.toString(), null)
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