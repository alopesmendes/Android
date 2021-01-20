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
    }

    fun close() {
        handler.close()
    }

    fun reset() {
        val db = handler.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseHandler.TABLE}");
        handler.onCreate(db);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addConfig(version: Int, content: String, tag: String): Config {
        val values = ContentValues()
        values.put(DatabaseHandler.KEY_VERSION, version)
        values.put(DatabaseHandler.KEY_CONTENT, content)
        values.put(DatabaseHandler.KEY_TAG, tag)
        values.put(DatabaseHandler.KEY_DATE, Date.valueOf(LocalDate.now().toString()).toString())

        val insertId = db.insert(DatabaseHandler.TABLE, null, values)
        val cursor = db.query(
            DatabaseHandler.TABLE,
            allColumns,
            "${DatabaseHandler.KEY_ID}=$insertId",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val config = cursorToConfig(cursor)
        cursor.close()
        return config
    }

    fun deleteConfig(id: Int) {
        db.delete(DatabaseHandler.TABLE, "${DatabaseHandler.KEY_ID} = $id", null)
    }

    fun getConnfig(id: Int): Config {
        val cursor = db.query(
            DatabaseHandler.TABLE, allColumns, "${DatabaseHandler.KEY_ID} = $id",
            null, null, null, null, null
        )

        cursor?.moveToFirst()

        val config =
            Config(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3))
        cursor.close()
        return config
    }

    fun getAllConfigs(): List<Config> {
        val configList = ArrayList<Config>()
        val cursor = db.query(DatabaseHandler.TABLE, allColumns, null, null, null, null, null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            configList.add(cursorToConfig(cursor))
            cursor.moveToNext()
        }
        cursor.close()
        return configList
    }

    fun updateConfig(config: Config): Int {
        val values = ContentValues()
        values.put(DatabaseHandler.KEY_CONTENT, config.content)

        return db.update(
            DatabaseHandler.TABLE,
            values,
            "${DatabaseHandler.KEY_ID} = ${config.id}",
            arrayOf(config.id.toString())
        )
    }

    private fun cursorToConfig(cursor: Cursor): Config {
        val id = cursor.getColumnIndex(DatabaseHandler.KEY_ID)
        val version = cursor.getColumnIndex(DatabaseHandler.KEY_VERSION)
        val content = cursor.getColumnIndex(DatabaseHandler.KEY_CONTENT)
        val tag = cursor.getColumnIndex(DatabaseHandler.KEY_TAG)
        return Config(
            cursor.getInt(id),
            cursor.getInt(version),
            cursor.getString(content),
            cursor.getString(tag)
        )
    }

}