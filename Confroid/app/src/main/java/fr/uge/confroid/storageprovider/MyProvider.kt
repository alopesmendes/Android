package fr.uge.confroid.storageprovider

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/***
 * The provider to write files and get them from the memory of the phone.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
object MyProvider {

    /***
     * Will write the [content] given on the [File].
     * @param context the [Context].
     * @param fileName the name of the [File].
     * @param content the data to overwrite on the [File].
     */
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

    /***
     * @param context the [Context].
     * @return Will return an [Array] of [File].
     */
    fun getFiles(context: Context) : Array<File> {
        return StorageUtils.getFiles(context.filesDir.absolutePath)
    }

}