package fr.umlv.test_confroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_display_config.*

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_config)

        val intent = intent
        if (intent != null) {
            var config: Config
            if (intent.hasExtra("config")) {
                config = intent.extras?.get("config") as Config
                AppNameConfig.text = config.app
                textVersionConfig.text = config.version.toString()
                textTagConfig.text = config.tag
                textContent.text = config.content
            }
        }
    }
}