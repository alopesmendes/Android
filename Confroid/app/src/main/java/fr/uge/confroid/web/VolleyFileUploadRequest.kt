package fr.uge.confroid.web

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import java.io.*
import kotlin.math.min

/**
 * Specific Volley Request to upload files.
 * Will use the idea of multipart.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
open class VolleyFileUploadRequest(
    method: Int,
    url: String,
    listener: Response.Listener<NetworkResponse>,
    errorListener: Response.ErrorListener) : Request<NetworkResponse>(method, url, errorListener) {
    private var responseListener: Response.Listener<NetworkResponse>? = null
    init {
        this.responseListener = listener
    }

    private var headers: Map<String, String>? = null
    private val divider: String = "--"
    private val ending = "\r\n"
    private val boundary = "apiclient-${System.currentTimeMillis()}"


    override fun getHeaders(): MutableMap<String, String> =
        when(headers) {
            null -> super.getHeaders()
            else -> headers!!.toMutableMap()
        }

    override fun getBodyContentType() = "multipart/form-data;boundary=$boundary"


    /**
     * Firstly will process the params.
     * Then it will process the data and write everything in dataOutputStream.
     * Finally it will return the ByteArray
     * @return ByteArray whit all the information of the files written.
     */
    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val dataOutputStream = DataOutputStream(byteArrayOutputStream)
        try {
            if (params != null && params.isNotEmpty()) {
                processParams(dataOutputStream, params, paramsEncoding)
            }
            val data = getByteData()
            if (data != null && data.isNotEmpty()) {
                processData(dataOutputStream, data)
            }
            dataOutputStream.writeBytes(divider + boundary + divider + ending)
            return byteArrayOutputStream.toByteArray()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return super.getBody()
    }

    /**
     * Needs to be rewritten.
     * Will be used to create the data we wand to send
     */
    @Throws(AuthFailureError::class)
    open fun getByteData(): Map<String, FileDataPart>? {
        return null
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
        return try {
            Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: NetworkResponse) {
        responseListener?.onResponse(response)
    }

    override fun deliverError(error: VolleyError) {
        errorListener?.onErrorResponse(error)
    }

    @Throws(IOException::class)
    private fun processParams(dataOutputStream: DataOutputStream, params: Map<String, String>, encoding: String) {
        try {
            params.forEach {
                dataOutputStream.writeBytes(divider + boundary + ending)
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"${it.key}\"$ending")
                dataOutputStream.writeBytes(ending)
                dataOutputStream.writeBytes(it.value + ending)
            }
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("Unsupported encoding not supported: $encoding with error: ${e.message}", e)
        }
    }

    /**
     * For each entry in data. We start by writing the basic information Content-Disposition etc
     * Then we write all the data content of a file in dataOutputStream.
     * @param dataOutputStream where we write all our data.
     * @param data the different files data.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun processData(dataOutputStream: DataOutputStream, data: Map<String, FileDataPart>) {
        data.forEach {
            val dataFile = it.value
            dataOutputStream.writeBytes("$divider$boundary$ending")
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"${it.key}\"; filename=\"${dataFile.fileName}\"$ending")
            if (dataFile.type != null && dataFile.type.trim().isNotEmpty()) {
                dataOutputStream.writeBytes("Content-Type: ${dataFile.type}$ending")
            }
            dataOutputStream.writeBytes(ending)
            val fileInputStream = ByteArrayInputStream(dataFile.data)
            var bytesAvailable = fileInputStream.available()
            val maxBufferSize = 1024 * 1024
            var bufferSize = min(bytesAvailable, maxBufferSize)
            val buffer = ByteArray(bufferSize)
            var bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize)
                bytesAvailable = fileInputStream.available()
                bufferSize = min(bytesAvailable, maxBufferSize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            }
            dataOutputStream.writeBytes(ending)
        }
    }
}

/**
 * A class to seperate the name of a file, it's data and type.
 */
class FileDataPart(var fileName: String?, var data: ByteArray, var type: String)