//package fr.uge.confroid.configurations;
//
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import fr.uge.confroid.R
//import kotlinx.android.synthetic.main.activity_all_versions.*
//
//
//class AllVersionsActivity : AppCompatActivity() {
//
//    val filter: IntentFilter = IntentFilter()
//    val broadcastAction = "getAllVersions"
//    private var configAdapter: ConfigAdapter? = null
//    private var configs: List<Config>? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_all_versions)
//        filter.addAction(broadcastAction)
//
//        if (intent != null) {
//            AppName.text = intent.getStringExtra("app")
//            val versions = intent.getSerializableExtra("versions")
//            configs = ((versions as Array<Config>).toList())
//            configAdapter = configs?.let { ConfigAdapter(this@AllVersionsActivity, it) }
//            RvAllVersions.adapter = configAdapter
//            RvAllVersions.layoutManager = LinearLayoutManager(this@AllVersionsActivity)
//            RvAllVersions.setHasFixedSize(true)
//
//            send_all_to_app_button.setOnClickListener {
//                val request = intent.getLongExtra("request", 0L)
//
//                Intent().apply {
//                    action = Intent.ACTION_SEND
//
//                    putExtra("content", configs?.joinToString("\n", "{", "}"))
//                    putExtra("request", request)
//
//                    startActivity(this)
//                }
//            }
//        }
//    }
//
//    fun onClickListener(position: Int) {
//        val intent = Intent(this, ConfigActivity::class.java)
//        intent.putExtra("config", configs?.get(position))
//        startActivity(intent)
//    }
//}