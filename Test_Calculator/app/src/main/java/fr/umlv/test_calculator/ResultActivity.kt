package fr.umlv.test_calculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                val request = intent.getIntExtra("request", -1)

                when (request) {
                    1 -> {
                        result_tv.text = intent.getStringExtra("content")
                    }
                    2 -> {
                        result_tv.text = intent.getStringExtra("content")
                    }
                    3 -> {
                        val token = intent.getLongExtra("token", 0)
                        prefs.edit().putLong("token", token).apply()
                        result_tv.text = token.toString()
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