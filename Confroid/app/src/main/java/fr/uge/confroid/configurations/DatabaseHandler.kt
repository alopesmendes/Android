package fr.uge.confroid.configurations

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * This class allows to create Confroid database with SQL syntax.
 * It contains a method to create a table for a new App,
 * and drop a table when an App cancels subscription.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class DatabaseHandler(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        const val DATABASE_VERSION: Int = 1
        const val DATABASE_NAME: String = "configDB"

        val TABLES: MutableSet<String> = mutableSetOf()
        const val KEY_ID: String = "id"
        const val KEY_VERSION: String = "version"
        const val KEY_CONTENT: String = "content"
        const val KEY_TAG: String = "tag"
        const val KEY_DATE: String = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TABLES.forEach {
            db?.execSQL("DROP TABLE IF EXISTS $it")
        }

        onCreate(db)
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