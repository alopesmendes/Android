package fr.umlv.test_calculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}