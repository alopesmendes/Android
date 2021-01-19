package fr.umlv.test_confroid

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.sql.Date
import java.time.LocalDate

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
        val KEY_VERSION: String = "version"
        val KEY_CONTENT: String = "content"
        val KEY_TAG: String = "tag"
        val KEY_DATE: String = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_VERSION INTEGER, $KEY_CONTENT TEXT, $KEY_TAG TEXT, $KEY_DATE DATE)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db?.execSQL("DROP TABLE IF EXISTS $TABLE");

        // Create tables again
        onCreate(db);
    }
}