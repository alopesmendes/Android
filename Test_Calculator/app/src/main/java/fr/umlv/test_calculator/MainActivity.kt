package fr.umlv.test_calculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_calculator.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_all_versions.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        CalculatorConfig.TOKEN = prefs.getString("token", "").toString()
        token_tv.text = "TOKEN: ${CalculatorConfig.TOKEN}"

        /////////////////////////////

        send_config_button.setOnClickListener {
            if (CalculatorConfig.TOKEN.isBlank()) {
                ConfroidUtils.subscribeConfiguration<Any>(this, "Calculator", null)
            } else {
                Intent(this, SendConfig::class.java).apply {
                    startActivity(this)
                }
            }
        }

        load_config_button.setOnClickListener {
            if (CalculatorConfig.TOKEN.isBlank()) {
                ConfroidUtils.subscribeConfiguration<Any>(this, "Calculator", null)
            } else {
                Intent(this, LoadConfig::class.java).apply {
                    startActivity(this)
                }
            }
        }

        all_versions_button.setOnClickListener {
            if (CalculatorConfig.TOKEN.isBlank()) {
                ConfroidUtils.subscribeConfiguration<Any>(this, "Calculator", null)
            } else {
                ConfroidUtils.getConfigurationVersions(this, "Calculator", null)
            }
        }

        cancel_sub_button.setOnClickListener {
            ConfroidUtils.cancelConfigurationSubscription<Any>(this, null)
            prefs.edit().putString("token", "").apply()
            token_tv.text = "TOKEN: ${CalculatorConfig.TOKEN}"
        }
    }
}