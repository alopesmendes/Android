package fr.umlv.test_calculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.umlv.test_calculator.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_load_config.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        when {
            intent.action == Intent.ACTION_SEND -> {
                val content = intent.getStringExtra("content")
                val token = intent.getStringExtra("token")
                val request = intent.getLongExtra("request", 0L)

                if (request == ConfroidUtils.REQUEST) {
                    result_tv.text =
                        "LAST REQUEST ID: ${ConfroidUtils.REQUEST}\nREQUEST ID: $request\n\n$content"

                    if (token != null) {
                        prefs.edit().putString("token", token).apply()
                        result_tv.text = token
                        Intent(this, MainActivity::class.java).apply {
                            putExtra("token", token)
                            startActivity(this)
                        }
                    }
                }
            }
        }
    }
}