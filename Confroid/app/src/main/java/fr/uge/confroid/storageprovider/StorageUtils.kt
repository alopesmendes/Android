package fr.uge.confroid.storageprovider

import android.util.Log
import java.io.File

/***
 * Will deal with the features of an file by creating or getting.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
object StorageUtils {

    /***
     * Will create a file. If the file already exists, it will delete the [File] first then create the [File].
     * @param fileName the [File] name.
     * @param folderName the folder name.
     * @return a [File]
     */
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

    /***
     * Will return all the [File] in a folder.
     * @param folderName the folder name.
     * @return An [Array] of [File].
     */
    fun getFiles(folderName: String?) : Array<File> {
        val root = File(folderName)
        if (!root.exists() && !root.isDirectory) {
            return arrayOf()
        }
        return root.listFiles()
    }
}