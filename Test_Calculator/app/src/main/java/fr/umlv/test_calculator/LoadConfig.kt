package fr.umlv.test_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.umlv.test_calculator.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_load_config.*

class LoadConfig : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_config)

        when {
            intent.action == Intent.ACTION_SEND -> {
                content_text_view.text = intent.getStringExtra("content")
            }
        }

        load_config_button.setOnClickListener {
            val version = version_edit_text.text.toString()
            if (version.isBlank()) {
                Toast.makeText(this, "version required", Toast.LENGTH_SHORT).show()
            } else {
                ConfroidUtils.loadConfiguration<Any>(this, "Calculator", version, null)
            }
        }
    }
}