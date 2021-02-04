package fr.umlv.test_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_all_versions.*

class AllVersions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_versions)

        when {
            intent.action == Intent.ACTION_SEND -> {
                all_versions_text_view.text = intent.getStringExtra("content")
            }
        }
    }
}