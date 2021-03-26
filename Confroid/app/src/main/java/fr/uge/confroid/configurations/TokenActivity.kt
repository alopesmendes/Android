package fr.uge.confroid.configurations

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.uge.confroid.R
import kotlinx.android.synthetic.main.activity_token.*
import kotlin.random.Random

/**
 * This Activity allows to send a token to an App and associate the App with the token
 * in Confroid preferences.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class TokenActivity : AppCompatActivity() {
    private var token: String = ""

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
                (1..STRING_LENGTH).map { Random.nextInt(0, REGEX.size) }.map(REGEX::get)
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