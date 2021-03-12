package fr.umlv.test_calculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.confroidutils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_load_config.*

class LoadConfig : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_config)

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