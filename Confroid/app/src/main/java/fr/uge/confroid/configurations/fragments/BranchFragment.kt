package fr.uge.confroid.configurations.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uge.confroid.R
import fr.uge.confroid.configurations.model.Field
import fr.uge.confroid.configurations.model.FieldAdapter
import kotlinx.android.synthetic.main.fragment_branch.*
import kotlinx.android.synthetic.main.fragment_branch.RvValueField

/**
 * This Fragment displays a field of a config when the field
 * contains other fields that are not primitive.
 * It shows its content as a RecyclerView and the user
 * can navigate into it.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class BranchFragment : Fragment(R.layout.fragment_branch) {

    lateinit var adapter: FieldAdapter
    private var positionToChange = -1
    private lateinit var field: Field

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            field = it.get("field") as Field
        }
        FieldNameValue.text = field.name
        adapter = field.recursiveContent?.let { FieldAdapter(ConfigFragment(),this@BranchFragment, it, false) }!!
        RvValueField.adapter = adapter
        RvValueField.layoutManager = LinearLayoutManager(activity)
        RvValueField.setHasFixedSize(true)
        button_modify.setOnClickListener{
                navigateBack()
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Field>(
            "updateField"
        )?.observe(viewLifecycleOwner, { field ->
            //Log.i("changement a faire", field.toString())
            //field.recursiveContent?.set(positionToChange, field)
            adapter.updatevalue(positionToChange,field)
            adapter.notifyItemChanged(positionToChange)
            //Log.i("changement",adapter.getfield(positionToChange).toString())
        })
    }

    private fun navigateBack() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "updateField",
            field
        )
        findNavController().popBackStack()
    }

    private fun navigateToLeaf(field: Field) {
        //Log.i("gotoLeaf", field.toString())
        val bundle = bundleOf("field" to field)
        findNavController().navigate(R.id.action_branchFragment_to_leafFragment, bundle)
    }

    private fun navigateToBranch(field: Field) {
        //Log.i("gotoLeaf", field.toString())
        val bundle = bundleOf("field" to field)
        findNavController().navigate(R.id.action_branchFragment_to_branchFragment, bundle)
    }

    fun onClickListener(adapterPosition: Int) {
        val field = adapter.getfield(adapterPosition)
        positionToChange = adapterPosition
        if (field.content.isNullOrBlank()){
            navigateToBranch(field)
        }else{
            navigateToLeaf(field)
        }
    }


}