package fr.umlv.test_confroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when {
            intent.action == Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    intent.getStringExtra("name").let {
                        tv4.text = it
                    }
                    intent.getStringExtra("content").let {
                        tv5.text = it
                    }
                    intent.getStringExtra("tag").let {
                        tv6.text = it
                    }
                }
            }
        }
    }
}