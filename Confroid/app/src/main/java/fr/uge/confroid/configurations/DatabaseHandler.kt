package fr.uge.confroid.configurations

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHandler(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val DATABASE_VERSION: Int = 1
        val DATABASE_NAME: String = "configDB"

        val TABLES: MutableSet<String> = mutableSetOf()
        val KEY_ID: String = "id"
        val KEY_VERSION: String = "version"
        val KEY_CONTENT: String = "content"
        val KEY_TAG: String = "tag"
        val KEY_DATE: String = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
//        initTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        TABLES.forEach {
            db?.execSQL("DROP TABLE IF EXISTS $it")
        }

        // Create tables again
        onCreate(db)
    }

    fun reset(db: SQLiteDatabase) {
        TABLES.forEach {
            db.execSQL("DROP TABLE IF EXISTS $it")
        }
        TABLES.removeAll(TABLES)
    }

    fun createTable(db: SQLiteDatabase, app: String) {
        if (!containsTable(app)) {
            db.execSQL("CREATE TABLE $app($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_VERSION INTEGER UNIQUE, $KEY_CONTENT TEXT, $KEY_TAG TEXT, $KEY_DATE DATE)")
            TABLES.add(app)
        }
    }

    fun initTables(db: SQLiteDatabase?) {
        val cursor = db?.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name != 'android_metadata'",
            null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    Log.i("tables", cursor.getString(cursor.getColumnIndex("name")))
                    TABLES.add(cursor.getString(cursor.getColumnIndex("name")))
                    cursor.moveToNext()
                }
            }
            cursor.close()
        }
    }

    fun containsTable(app: String): Boolean {
        return TABLES.contains(app)
    }

    fun removeApp(app: String) {
        TABLES.remove(app)
    }
}