package fr.uge.confroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textViewRegisterNow.setOnClickListener {
            Intent(this, MainActivity::class.java).apply { startActivity(this) }
            finish()
        }
    }
}