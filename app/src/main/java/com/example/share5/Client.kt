package com.example.share5

import android.content.ContentResolver
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import java.io.*
import java.net.InetAddress
import java.net.Socket
import kotlin.math.round


suspend fun Client(context: Context, device: InetAddress?, activity: MainActivity) {

    val directory = activity.getOutputDirectory()

    withContext(Dispatchers.IO) {
        try {
            // Connect to server
            Log.d("App", "Client connecting...")
            val clientSocket = Socket(device, 37682)
            Log.d("App", "Client connected")

            // Receive the number of files from the server
            val inputStream = clientSocket.getInputStream()
            val dataInputStream = DataInputStream(inputStream)

            val outputStream = clientSocket.getOutputStream()
            val dataOutputStream = DataOutputStream(outputStream)
            val numFiles = dataInputStream.readInt()
            activity.numFiles = numFiles
            Log.d("App", "Number of files = $numFiles")

            // Receive each file
            for (i in 0 until numFiles) {
                // Receive the length of the file name
                val fileNameLength = dataInputStream.readInt()
                // Receive the file name
                val fileNameBytes = ByteArray(fileNameLength)
                dataInputStream.readFully(fileNameBytes)
                val fileName = String(fileNameBytes)
                val size = dataInputStream.readLong()
                Log.d("App", "File name = $fileName")
                // Receive the file data from the server
                val fileSize = byteArrayToUri(context.contentResolver, directory, fileName, clientSocket.getInputStream(),size,activity).toDouble()/1024/1024
                Log.d("App", "File received: $fileSize MB")

            }

            Log.d("App","out of loop")

            // Close the connection
            clientSocket.close()
        } catch (e: IOException) {
            Log.e("App", "Failed to connect to server: ${e.message}")
        }
    }
}


private fun receiveByteArray(inputStream: InputStream): ByteArray {
    val dataInputStream = DataInputStream(inputStream)
    val byteArraySize = dataInputStream.readInt()
    val byteArray = ByteArray(byteArraySize)
    dataInputStream.readFully(byteArray)
    return byteArray
}

private fun byteArrayToUri(contentResolver: ContentResolver, directory: File, fileName: String, inputStream: InputStream, size :Long,activity: MainActivity): Long {
    val file = File(directory, fileName)
    val outputStream = FileOutputStream(file)
    var fileSize = 0L
    var last  = 0.0

    try {
        val startTime = System.currentTimeMillis()
        var byteArray = receiveByteArray(inputStream)
        while (byteArray.isNotEmpty()&& fileSize < size) {
            outputStream.write(byteArray)
            fileSize += byteArray.size

            val endTime = System.currentTimeMillis()
            val timeTaken = (endTime - startTime) / 1000.0 // in seconds

            if (fileSize > 0) {
                activity.cnet= (round(fileSize / (timeTaken*1000000) *100)/100).toFloat()
                last = timeTaken

            }
            if (fileSize < size) {
                byteArray = receiveByteArray(inputStream)
            }
        }
        activity.cnet =0f

        Log.d("App", "File saved: ${file.absolutePath}, $fileSize bytes")
    } catch (e: IOException) {
        Log.e("App", "Failed to save file: ${e.message}")
    } finally {
        try {
            outputStream.close()
        } catch (e: IOException) {
            Log.e("App", "Failed to close output stream: ${e.message}")
        }
    }

    return fileSize
}





