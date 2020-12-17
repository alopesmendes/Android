package fr.umlv.test_calculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_send_config.*

class SendConfig : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_config)

        send_to_confroid.setOnClickListener {
            val content = Content()

            content.init(ed21.text.toString() + ed22.text.toString() + ed23.text.toString())
            content.convertToBundle()

            data_tv.text = content.data.toString()
            content_tv2.text = content.content.toString()

            Intent().apply {
                action = Intent.ACTION_SEND

                putExtra("name", packageName + "." + ed1.text.toString())
                putExtra("content", content.content)
                putExtra("tag", ed3.text.toString())

                startActivity(this)
            }

        }
    }
}