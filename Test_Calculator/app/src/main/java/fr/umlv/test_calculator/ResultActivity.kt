package fr.umlv.test_calculator

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
                        result_tv.text = intent.getLongExtra("token", 0).toString()
                        CalculatorConfig.TOKEN = intent.getLongExtra("token", 0)
                        Intent(this, MainActivity::class.java).apply {
                            startActivity(this)
                        }
                    }
                }
            }
        }
    }
}