package fr.umlv.test_shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.confroidutils.ConfroidUtils
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

                result_tv.text =
                    "LAST REQUEST ID: ${ConfroidUtils.REQUEST}\nREQUEST ID: $request\n\n"
                if (request == ConfroidUtils.REQUEST) {
                    prefs.edit().putLong("request", 0L).apply()
                    result_tv.text = result_tv.text.toString().plus("$content")

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