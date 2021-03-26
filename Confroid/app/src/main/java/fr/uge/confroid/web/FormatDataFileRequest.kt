package fr.uge.confroid.web

import java.io.ByteArrayInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.math.min

/**
 * A class to that will deal with the data of a file.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
class FormatDataFileRequest(var fileName: String, var data: ByteArray) {

    companion object {
        private val divider: String = "--"
        private val ending = "\r\n"
        private val boundary = "files-${System.currentTimeMillis()}"

        /**
         * For each entry in data. We start by writing the basic information Content-Disposition etc
         * Then we write all the data content of a file in dataOutputStream.
         * @param dos where we write all our data.
         * @param data the different files data.
         * @throws IOException
         */
        @Throws(IOException::class)
        fun processData(dos: DataOutputStream, data: FormatDataFileRequest) {

            dos.writeBytes("$divider$boundary$ending")
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${data.fileName}\"$ending")
            dos.writeBytes(ending)

            val fileInputStream = ByteArrayInputStream(data.data)
            var bufferSize = min(fileInputStream.available(), DEFAULT_BUFFER_SIZE)
            val buffer = ByteArray(bufferSize)
            while (fileInputStream.read(buffer, 0, bufferSize) > 0) {
                dos.write(buffer, 0, bufferSize)
                bufferSize = min(fileInputStream.available(), DEFAULT_BUFFER_SIZE)
            }
            dos.writeBytes(ending)
            dos.writeBytes("$divider$boundary$divider$ending")
        }

        /***
         * @return the bodyContentType
         */
        fun bodyContentType() : String = "multipart/form-data;boundary=$boundary"
    }
}
