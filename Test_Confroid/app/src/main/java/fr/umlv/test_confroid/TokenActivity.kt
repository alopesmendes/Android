package fr.umlv.test_confroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.umlv.test_confroid.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_all_versions.*
import kotlinx.android.synthetic.main.activity_token.*
import kotlin.random.Random

class TokenActivity : AppCompatActivity() {
    var token: String = ""

    companion object {
        const val STRING_LENGTH: Int = 20
        val REGEX: List<Char> = ('a'..'z') + ('A'..'Z')
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        //////////////////////////////////

        generate_token_button.setOnClickListener {
            token =
                (1..STRING_LENGTH).map { i -> Random.nextInt(0, REGEX.size) }.map(REGEX::get)
                    .joinToString("")
            token_tv.text = "TOKEN: $token"
        }

        if (intent != null) {
            val app = intent.extras?.getString("app")
            val request = intent.getLongExtra("request", 0L)
            app_tv.text = app

            send_token_button.setOnClickListener {
                if (token != "") {
                    prefs.edit().putString("$app", token).apply()
                    Intent().apply {
                        action = Intent.ACTION_SEND

                        putExtra("token", token)
                        putExtra("request", request)

                        startActivity(this)
                    }
                }
            }
        }


    }
}