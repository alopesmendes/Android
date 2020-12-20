package fr.umlv.test_confroid

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import java.net.IDN

class DatabaseHandler(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val DATABASE_VERSION: Int = 1
        val DATABASE_NAME: String = "configDB"
        val TABLE: String = "configs"
        val KEY_ID: String = "id"
        val KEY_CONTENT: String = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE($KEY_ID INTEGER PRIMARY KEY, $KEY_CONTENT TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db?.execSQL("DROP TABLE IF EXISTS $TABLE");

        // Create tables again
        onCreate(db);
    }

    fun addConfig(config: Config) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_CONTENT, config.content)

        db.insert(TABLE, null, values)
        db.close()
    }

    fun getContact(id: Int): Config {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE, arrayOf(KEY_CONTENT), KEY_ID + "=?",
            arrayOf(id.toString()), null, null, null, null
        )

        cursor?.moveToFirst()

        val config = Config(cursor.getInt(0), cursor.getString(1))
        return config
    }

    fun getAllConfigs(): List<Config> {
        val configList = ArrayList<Config>()
        val selectQuery = "SELECT * FROM " + TABLE
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val config = Config(cursor.getInt(0), cursor.getString(1))
                configList.add(config)
            } while (cursor.moveToNext())
        }
        return configList
    }

    fun updateConfig(config: Config): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_CONTENT, config.content)

        return db.update(TABLE, values, KEY_ID + "=?", arrayOf(config.id.toString()))
    }

    fun deleteConfig(config: Config) {
        val db = this.writableDatabase
        db.delete(TABLE, KEY_ID + "=?", arrayOf(config.id.toString()))
        db.close()
    }

    fun getConfigsCount(): Int {
        val countQuery = "SELECT * FROM " + TABLE
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)

        return cursor.count
    }
}