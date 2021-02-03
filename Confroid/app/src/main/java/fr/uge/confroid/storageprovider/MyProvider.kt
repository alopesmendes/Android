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


class MyProvider() {
    companion object {
        private const val AUTHORITIES = "fr.uge.confroid.storageprovider"
    }

    /**
     * Create and start intent to share a standard text value.
     */
    fun onShareText(context: Context) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is a text I'm sharing.")
        context.startActivity(Intent.createChooser(shareIntent, "Share..."))
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun shareFile(context: Context) {
        val file = StorageUtils.getFileFromStorage(context, "mmtext.txt", context.filesDir.absolutePath)


        try {
            val outputStream = FileOutputStream(file);
            outputStream.write("On est la".toByteArray());
            outputStream.close();
        } catch (e: IOException) {
            //Handle exception
        }

        Log.i("file", "created $file")
        val contentUri = getUriForFile(context, AUTHORITIES, file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_OPEN_DOCUMENT
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "Share ..."))
    }

    fun getFile(context: Context) : File {
        val file = StorageUtils.getFileFromStorage(context, "mmtext.txt", context.filesDir.absolutePath)


        try {
            val outputStream = FileOutputStream(file);
            outputStream.write("On est la".toByteArray());
            outputStream.close();
        } catch (e: IOException) {
            //Handle exception
        }
        return file
    }

}