package fr.umlv.test_confroid

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import fr.umlv.test_confroid.services.ConfigurationPusher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    companion object {
        lateinit var model: Model
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = Model(this)
        model.open()
        model.reset()

//        when {
//            intent.action == Intent.ACTION_SEND -> {

//            }
//        }

        /////////////////////////////////////////////////////

        insert_button.setOnClickListener {
            Log.i("main", "pusher button")
            val version = version_editText.text.toString()
            val content = content_editText.text.toString()
            val tag = tag_editText.text.toString()

            if (version.isBlank() || content.isBlank()) {
                Toast.makeText(this, "version and content required", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "configuration added", Toast.LENGTH_SHORT).show()
                Intent(this, ConfigurationPusher::class.java).apply {
                    putExtra("version", version.toInt())
                    putExtra("content", content)
                    putExtra("tag", tag)

                    startService(this)
                }
            }
        }

        delete_button.setOnClickListener {
            val id = id_editText.text.toString()
            if (id.isBlank()) {
                Toast.makeText(this, "configuration id required", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "configuration deleted", Toast.LENGTH_SHORT).show()
                model.deleteConfig(id.toInt())
            }
        }

        select_all_button.setOnClickListener {
            val configs = model.getAllConfigs()
            test1.text = configs.joinToString("\n", "{", "}")
        }
    }
}