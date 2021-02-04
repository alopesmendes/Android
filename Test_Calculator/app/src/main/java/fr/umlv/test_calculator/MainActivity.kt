package fr.umlv.test_calculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_confroid.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        send_config_button.setOnClickListener {
            Intent(this, SendConfig::class.java).apply {
                startActivity(this)
            }
        }

        load_config_button.setOnClickListener {
            Intent(this, LoadConfig::class.java).apply {
                startActivity(this)
            }
        }

        all_versions_button.setOnClickListener {
            ConfroidUtils.getConfigurationVersions(this, "Calculator", null)
        }
    }
}