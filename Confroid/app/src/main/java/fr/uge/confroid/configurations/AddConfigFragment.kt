package fr.uge.confroid.configurations

import android.os.Bundle
import android.util.Log
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
    var newversion:Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var app:String =  ""
        var oldversion:Int = 0

        arguments?.let {
            fields = it.get("fields") as ArrayList<Field>
            app = it.getString("app_name").toString()
            oldversion = it.getInt("version_number")
        }
        app_name.text = app
        newContentJsonFormat.text = toJsonFormat()
        editTag.hint = "Version $oldversion modified"


        finalizeConfig.setOnClickListener{
            if (editVersion.text.isNotEmpty()){
                newversion = editVersion.text.toString() as Int
                var config:Config = Config(app, 0, newversion, toJsonFormat(), editTag.text.toString())
            }
        }
    }

    companion object {
    }

    fun toJsonFormat(): String {
        var res:StringBuilder = java.lang.StringBuilder()
        res.append("{")
        for(i in fields.indices){
            Log.i("field a insérer", fields[i].toString())
            res.append(fields[i].toJsonFormat())
            if (i < fields.size-1){
                res.append(",")
            }
        }
        res.append("}")
        return res.toString()
    }
}