package fr.umlv.test_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra("name", ed1.text.toString())

//                utiliser setX() pour le content
                putExtra("content", ed2.text.toString())

                putExtra("tag", ed3.text.toString())
                type = "text/plain"
            }
            startActivity(sendIntent)
        }
    }
}