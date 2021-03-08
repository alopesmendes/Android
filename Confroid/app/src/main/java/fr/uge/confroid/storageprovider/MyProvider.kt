package fr.uge.confroid.storageprovider

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider.getUriForFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object MyProvider {


    fun writeFile(context: Context, fileName : String, content : ByteArray) : File {
        val file = StorageUtils.createFile(fileName, context.filesDir.absolutePath)
        Log.i("file path", "path $file")

        try {
            val outputStream = FileOutputStream(file);
            outputStream.write(content);
            outputStream.close();
        } catch (e: IOException) {
            //Handle exception
        }
        return file
    }

    fun getFiles(context: Context) : Array<File> {
        return StorageUtils.getFiles(context.filesDir.absolutePath)
    }

}