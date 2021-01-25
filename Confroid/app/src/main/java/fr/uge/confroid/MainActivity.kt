package fr.uge.confroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import fr.uge.confroid.web.LoginActivity
import fr.uge.confroid.web.SharedPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display's the username and the encrypted password if log
        // Bug : Does not work if we close the application.
        if (SharedPreferences.getInstance(this).isLoggedIn()) {
            val user = SharedPreferences.getInstance(this).getUser()
            usernameTextMain.visibility = View.VISIBLE
            passwordTextMain.visibility = View.VISIBLE
            usernameTextMain.text = user.username
            passwordTextMain.text = user.password

        } else {
            usernameTextMain.visibility = View.INVISIBLE
            passwordTextMain.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.register_login, menu)
        menu?.findItem(R.id.logoutItem)?.isVisible = SharedPreferences.getInstance(this).isLoggedIn()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.loginItem -> {
                Intent(this, LoginActivity::class.java).apply { startActivity(this) }
                true
            }
            R.id.logoutItem -> {
                SharedPreferences.getInstance(this).logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}