package fr.umlv.test_calculator

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_calculator.config.CalculatorPreferences
import fr.umlv.test_calculator.reflect.BillingDetail
import fr.umlv.test_calculator.reflect.ShippingAddress
import fr.umlv.test_calculator.reflect.ShoppingInfo
import fr.umlv.test_calculator.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_send_config.*
import kotlinx.android.synthetic.main.activity_send_config.version_edit_text

class SendConfig : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_config)

        /////////////////////////////////////////////////////////

        val prefs = CalculatorPreferences("Standard", "Light", true)

        //////////////////////////////////////////////

        ArrayAdapter.createFromResource(
            this,
            R.array.calc_spinner,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            calc_sp.adapter = it
        }
        calc_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    prefs.calculator = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        /////////////////////////////////////////////////

        ArrayAdapter.createFromResource(
            this,
            R.array.mode_spinner,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mode_sp.adapter = it
        }
        mode_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    prefs.lightMode = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        /////////////////////////////////////////////

        historic_cb.setOnClickListener {
            prefs.removeHistoricAtStart = historic_cb.isChecked
        }

        //////////////////////////////////////////

        show_config_button.setOnClickListener {
            val fields = ConfroidUtils.deepGetFields(mutableMapOf(), prefs)
            val content = ConfroidUtils.convertToBundle(fields)
            config_text_view.text = content.toString()
        }

        /////////////////////////////////////////////////////////

        send_to_confroid_button.setOnClickListener {
            val version = version_edit_text.text.toString()
            if (version.isBlank()) {
                Toast.makeText(this, "version required", Toast.LENGTH_SHORT).show()
            } else {
                ConfroidUtils.saveConfiguration(this, "Calculator", prefs, version)
            }
        }
    }
}