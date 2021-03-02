package fr.umlv.test_confroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.umlv.test_confroid.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_all_versions.*
import kotlinx.android.synthetic.main.activity_token.*
import kotlin.random.Random

class TokenActivity : AppCompatActivity() {
    var token: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)

        val intent = intent
        if (intent != null) {
            app_tv.text = intent.extras?.getString("app")
        }

        generate_token_button.setOnClickListener {
            token = Random.nextLong()
            token_tv.text = "TOKEN: $token"
        }

        send_token_button.setOnClickListener {
            if (token == 0L) {
                Toast.makeText(this, "token required", Toast.LENGTH_SHORT).show()
            } else {
                Intent().apply {
                    action = Intent.ACTION_SEND

                    putExtra("token", token)
                    putExtra("request", 3)

                    startActivity(this)
                }
            }
        }

    }
}