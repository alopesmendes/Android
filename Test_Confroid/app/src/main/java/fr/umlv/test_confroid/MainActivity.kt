package fr.umlv.test_confroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DatabaseHandler(
            this,
            DatabaseHandler.DATABASE_NAME,
            null,
            DatabaseHandler.DATABASE_VERSION
        )

        when {
            intent.action == Intent.ACTION_SEND -> {
                intent.getStringExtra("name").let {
                    tv4.text = it
                }

                intent.getSerializableExtra("content").let {
                    tv5.text = it.toString()
                    db.addConfig(Config(1, it.toString()))
                    db.addConfig(Config(2, it.toString()))
                    db.addConfig(Config(3, it.toString()))
                    db.addConfig(Config(4, it.toString()))
                    db.addConfig(Config(5, it.toString()))
                }

                intent.getStringExtra("tag").let {
                    tv6.text = it
                }
            }
        }

        val configs = db.getAllConfigs()
        test1.text = configs[0].toString()
        test2.text = configs[1].toString()
        test3.text = configs[2].toString()
        test4.text = configs[3].toString()
        test5.text = configs[4].toString()
    }
}