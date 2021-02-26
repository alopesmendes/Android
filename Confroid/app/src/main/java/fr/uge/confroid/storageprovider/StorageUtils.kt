package fr.uge.confroid.storageprovider

import android.content.Context
import android.util.Log
import java.io.File

object StorageUtils {

    fun createFile(
        fileName: String?,
        folderName: String?
    ) : File {
        val root = File(folderName)
        if (!root.exists()) {
            root.mkdirs()
        }
        val file: File = File(root, fileName)
        if (file.exists()) {
            file.delete()
        }

        if (!file.createNewFile()) {
            Log.e("file", "could not create file")
        }
        return file
    }

    fun getFiles(folderName: String?) : Array<File> {
        val root = File(folderName)
        if (!root.exists() && !root.isDirectory) {
            return arrayOf()
        }
        return root.listFiles()
    }
}