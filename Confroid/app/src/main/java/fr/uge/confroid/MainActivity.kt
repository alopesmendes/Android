package fr.uge.confroid

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import fr.uge.confroid.configurations.AppFragment
import fr.uge.confroid.settings.SettingFragment
import fr.uge.confroid.web.*
import fr.uge.confroid.worker.UploadWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SettingFragment.enableMode(this)


        if (WebSharedPreferences.getInstance(this).isLoggedIn()) {
            /*
            MyProvider.writeFile(applicationContext, "mmtext.txt", "On est la".toByteArray())
            MyProvider.writeFile(applicationContext, "save.txt", "Sauvegarde les secrets".toByteArray())
            MyProvider.writeFile(applicationContext, "allo.txt", "Allo les gens".toByteArray())
            MyProvider.writeFile(applicationContext, "bonjour.txt", "Bonjour les amis".toByteArray())
            MyProvider.writeFile(applicationContext, "anime.txt", "Cest bien les animes".toByteArray())
            MyProvider.writeFile(applicationContext, "cours.txt", "Pas ouf".toByteArray())
            MyProvider.writeFile(applicationContext, "eau.txt", "H20".toByteArray())
            MyProvider.writeFile(applicationContext, "avatar.txt", "Les 4 elements".toByteArray())

             */

            val user = WebSharedPreferences.getInstance(this).getUser()
            LoginRequest.request(this, user.username, user.password) {}
            work()

            Log.i("shared user", user.toString())

        }


        isLoggedInVisibility()

        setSupportActionBar(mainToolbar)

        mainNavigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(this, mainDrawerLayout, mainToolbar, R.string.navigation_open_drawer, R.string.navigation_close_drawer)
        mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        mainNavigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, AppFragment()).commit()
            mainNavigationView.setCheckedItem(R.id.homeItem)
        }

    }

    private fun isLoggedInVisibility() {
        if (WebSharedPreferences.getInstance(applicationContext).isLoggedIn()) {
            mainNavigationView.menu.findItem(R.id.logoutItem).isVisible = true
            mainNavigationView.menu.findItem(R.id.filesItem).isVisible = true
            mainNavigationView.menu.findItem(R.id.loginItem).isVisible = false
        } else {
            mainNavigationView.menu.findItem(R.id.logoutItem).isVisible = false
            mainNavigationView.menu.findItem(R.id.filesItem).isVisible = false
            mainNavigationView.menu.findItem(R.id.loginItem).isVisible = true
        }
    }

    override fun onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction().addToBackStack(null)
        when(item.itemId) {
            R.id.homeItem -> {
                transaction.replace(R.id.mainFrameLayout, AppFragment()).commit()
            }
            R.id.filesItem -> {
                transaction.replace(R.id.mainFrameLayout, FilesFragment()).commit()
            }
            R.id.loginItem -> {
                transaction.replace(R.id.mainFrameLayout, LoginFragment()).commit()
            }
            R.id.logoutItem -> {
                WebSharedPreferences.getInstance(applicationContext).logout()
            }

            R.id.settingItem -> {
                transaction.replace(R.id.mainFrameLayout, SettingFragment()).commit()
            }
        }
        mainDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun work() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val work = PeriodicWorkRequestBuilder<UploadWorker>(20, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(work)
    }
/*
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

    private fun parse(result: String) : List<FileAttributes> {
        val list = Gson().fromJson(result, List::class.java)
        return list.map {
            val jsonObject = it as Map<String, String>
            FileAttributes(jsonObject["name"] ?: error(""), jsonObject["url"] ?: error(""))
        }
    }

    private fun getFiles(token: String) {
        val filesRequest = object : StringRequest(Method.GET, URL.ROOT_FILES,
            {
                runBlocking {
                    updateAdapter(it)
                }

            },
            {
                Log.e("files error", it.toString())
                SharedPreferences.getInstance(this).logout()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(filesRequest)
    }

    private fun getFile(name : String, password: String, token : String) {
        val customRequest : CustomRequest<ByteArray> = @RequiresApi(Build.VERSION_CODES.O)
        object : CustomRequest<ByteArray>(Method.GET, "${URL.ROOT_FILE}/$name", ByteArray::class.java,
            {
                try {
                    if (it != null) {
                        val secretKey : SecretKey = SecretKeySpec(CryptKey.decrypt(password.toByteArray(), CryptKey.secretKey), "AES")
                        val decrypted = CryptKey.decrypt(it, secretKey)

                        val outputStream = openFileOutput(name, Context.MODE_PRIVATE)
                        outputStream.write(decrypted)
                        outputStream.close()
                        val file = File(filesDir, name)
                        //CryptKey.decryptFile(file)
                        //Log.i("content", "after->${String(CryptKey.decrypt(it)!!)}")
                        Toast.makeText(this, "Download completed", Toast.LENGTH_LONG).show()
                        Log.i("file", "content:${file.readText()}")
                    }
                } catch (e : Exception) {
                    Log.e("KEY_ERROR", "UNABLE TO DOWNLOAD FILE")
                    e.printStackTrace()
                }
            },
            {
                Log.e("error get", it.toString())
                SharedPreferences.getInstance(this).logout()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("authorization" to token)
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<ByteArray> {
                return Response.success( response?.data, HttpHeaderParser.parseCacheHeaders(response));
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(customRequest)
    }

    private suspend fun updateAdapter(requestBody : String) {
        val files = parse(requestBody)
        ArrayAdapter(
            this, android.R.layout.simple_spinner_item, files
        ).also {
            arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                requestSpinner.adapter = arrayAdapter
        }
        requestSpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val user = SharedPreferences.getInstance(this).getUser()
        val fileAttributes : FileAttributes = parent?.getItemAtPosition(position) as FileAttributes
        getFile(fileAttributes.name, user.password, user.token)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
    */
}