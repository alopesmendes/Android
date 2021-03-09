package fr.umlv.test_shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_shop.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_all_versions.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        ConfroidUtils.TOKEN = prefs.getString("token", "").toString()
        token_tv.text = "TOKEN: ${ConfroidUtils.TOKEN}"

        /////////////////////////////

        send_config_button.setOnClickListener {
            if (ConfroidUtils.TOKEN.isBlank()) {
                ConfroidUtils.subscribeConfiguration<Any>(this, "Shop", null)
            } else {
                Intent(this, SendConfig::class.java).apply {
                    startActivity(this)
                }
            }
        }

        load_config_button.setOnClickListener {
            if (ConfroidUtils.TOKEN.isBlank()) {
                ConfroidUtils.subscribeConfiguration<Any>(this, "Shop", null)
            } else {
                Intent(this, LoadConfig::class.java).apply {
                    startActivity(this)
                }
            }
        }

        all_versions_button.setOnClickListener {
            if (ConfroidUtils.TOKEN.isBlank()) {
                ConfroidUtils.subscribeConfiguration<Any>(this, "Shop", null)
            } else {
                ConfroidUtils.getConfigurationVersions(this, "Shop", null)
            }
        }

        cancel_sub_button.setOnClickListener {
            ConfroidUtils.cancelConfigurationSubscription<Any>(this, null)
            prefs.edit().putString("token", "").apply()
            token_tv.text = "TOKEN: ${ConfroidUtils.TOKEN}"
        }
    }
}