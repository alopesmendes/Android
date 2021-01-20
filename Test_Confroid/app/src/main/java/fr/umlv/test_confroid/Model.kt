package fr.umlv.test_confroid

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Date
import java.time.LocalDate


class Model(context: Context) {

    lateinit var db: SQLiteDatabase
    private val handler: DatabaseHandler = DatabaseHandler(
        context,
        DatabaseHandler.DATABASE_NAME,
        null,
        DatabaseHandler.DATABASE_VERSION
    )
    val allColumns: Array<String> = arrayOf(
        DatabaseHandler.KEY_ID,
        DatabaseHandler.KEY_VERSION,
        DatabaseHandler.KEY_CONTENT,
        DatabaseHandler.KEY_TAG,
        DatabaseHandler.KEY_DATE
    )

    fun open() {
        db = handler.writableDatabase
        handler.initTables(db)
    }

    fun close() {
        handler.close()
    }

    fun reset() {
        handler.reset(db)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addConfig(app: String, version: Int, content: String, tag: String): Config {
        handler.createTable(db, app)

        val values = ContentValues()
        values.put(DatabaseHandler.KEY_VERSION, version)
        values.put(DatabaseHandler.KEY_CONTENT, content)
        values.put(DatabaseHandler.KEY_TAG, tag)
        values.put(DatabaseHandler.KEY_DATE, Date.valueOf(LocalDate.now().toString()).toString())

        val insertId = db.insert(app, null, values)
        val cursor = db.query(
            app,
            allColumns,
            "${DatabaseHandler.KEY_ID}=$insertId",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val config = cursorToConfig(app, cursor)
        cursor.close()
        return config
    }

    fun deleteConfig(app: String, id: Int) {
        db.delete(app, "${DatabaseHandler.KEY_ID} = $id", null)
    }

    fun getConfig(app: String, id: Int): Config? {
        val cursor = db.query(
            app, allColumns, "${DatabaseHandler.KEY_ID} = $id",
            null, null, null, null, null
        )

        if (cursor == null) {
            return null
        } else {
            cursor.moveToFirst()

            val config =
                Config(
                    app,
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3)
                )
            cursor.close()
            return config
        }
    }

    fun getAllConfigs(): List<Config> {
        val configList = ArrayList<Config>()

        DatabaseHandler.TABLES.forEach {
            val cursor = db.query(it, allColumns, null, null, null, null, null)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                configList.add(cursorToConfig(it, cursor))
                cursor.moveToNext()
            }
            cursor.close()
        }
        return configList
    }

    fun updateConfig(app: String, config: Config): Int {
        val values = ContentValues()
        values.put(DatabaseHandler.KEY_CONTENT, config.content)

        return db.update(
            app,
            values,
            "${DatabaseHandler.KEY_ID} = ${config.id}",
            arrayOf(config.id.toString())
        )
    }

    fun showTables(): List<String> {
        val allTables = mutableListOf<String>()
        val cursor = db.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name != 'android_metadata'",
            null
        )

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                allTables.add(cursor.getString(cursor.getColumnIndex("name")))
                cursor.moveToNext()
            }
            cursor.close()
        }
        return allTables
    }

    private fun cursorToConfig(app: String, cursor: Cursor): Config {
        val id = cursor.getColumnIndex(DatabaseHandler.KEY_ID)
        val version = cursor.getColumnIndex(DatabaseHandler.KEY_VERSION)
        val content = cursor.getColumnIndex(DatabaseHandler.KEY_CONTENT)
        val tag = cursor.getColumnIndex(DatabaseHandler.KEY_TAG)
        return Config(
            app,
            cursor.getInt(id),
            cursor.getInt(version),
            cursor.getString(content),
            cursor.getString(tag)
        )
    }
}