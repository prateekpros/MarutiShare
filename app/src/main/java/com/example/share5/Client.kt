package com.example.share5

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket



//
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
                val fileSize = byteArrayToUri(context.contentResolver, directory, fileName, clientSocket.getInputStream(),size).toDouble()/1024/1024
                Log.d("App", "File received: $fileSize MB")
               // dataOutputStream.writeInt(1)
//                val delimiter = dataInputStream.readByte()
//                if (delimiter == '\n'.toByte()) {
//                    Log.e("App", " file delimiter not found")
//                   // break
//                }

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

private fun byteArrayToUri(contentResolver: ContentResolver, directory: File, fileName: String, inputStream: InputStream, size :Long): Long {
    val file = File(directory, fileName)
    val outputStream = FileOutputStream(file)
    var fileSize = 0L

    try {
        var byteArray = receiveByteArray(inputStream)
        while (byteArray.isNotEmpty()&&fileSize < size) {
            outputStream.write(byteArray)
            fileSize += byteArray.size
            if (fileSize < size) {
                byteArray = receiveByteArray(inputStream)
            }
        }

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

//


//@RequiresApi(Build.VERSION_CODES.M)
//suspend fun Server(context: Context, uris: List<Uri>, fileNames: List<String>, activity: MainActivity) {
//    withContext(Dispatchers.IO) {
//        try {
//            // Create server socket
//            Log.d("App", "Server on")
//            val serverSocket = ServerSocket(37682)
//            serverSocket.soTimeout=600000
//
//            // Wait for a client to connect
//            val clientSocket = serverSocket.accept()
//            Log.d("App", "Client connected")
//
//
//            // Send the number of files to the client.
//            val numFiles = uris.size
//            val outputStream = clientSocket.getOutputStream()
//            val inputStream = clientSocket.getInputStream()
//            val dataInputStream = DataInputStream(inputStream)
//            val dataOutputStream = DataOutputStream(outputStream)
//            dataOutputStream.writeInt(numFiles)
//
//            //-----------------
//            // Send each file
//            for (i in uris.indices) {
//                val uri = uris[i]
//                val fileName = fileNames[i]
//                Log.d("App","file name  = $fileName")
//                // Send the file name and size
//                dataOutputStream.writeInt(fileName.length)
//                dataOutputStream.writeBytes(fileName)
//
//                // Send the file contents
//                uriToByteArray(context.contentResolver, uri, outputStream, activity)
//                val confirmation = dataInputStream.readInt()
//                if (confirmation == 1) {
//                    Log.e("App", "File transfer confirmation from client")
//
//                }
//
//            }
//            // Close the connection
//            clientSocket.close()
//            serverSocket.close()
//        } catch (e: IOException) {
//            Log.e("App", "Failed to start server: ${e.message}")
//        }
//    }
//}
//
//
//private fun sendByteArray(outputStream: OutputStream, byteArray: ByteArray) {
//    val dataOutputStream = DataOutputStream(outputStream)
//    dataOutputStream.writeInt(byteArray.size)
//    dataOutputStream.write(byteArray)
//
//}
//
//
//@RequiresApi(Build.VERSION_CODES.M)
//private fun uriToByteArray(contentResolver: ContentResolver, uri: Uri, outputStream: OutputStream, activity: MainActivity):Long{
//    var fileSize = 0L
//    var inputStream: InputStream? = null
//
//
//    try {
//        inputStream = contentResolver.openInputStream(uri)
//        val buffer = ByteArray(BUFFER_SIZE)
//        var bytesRead: Int
//
//        if (inputStream != null) {
//
//            var totalBytesRead = 0L
//            val startTime = System.currentTimeMillis()
//
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                totalBytesRead += bytesRead
//                sendByteArray(outputStream, buffer.copyOf(bytesRead))
//                fileSize += bytesRead
//            }
//
//        }
//
//
//        Log.d("App", "File read: $fileSize bytes")
//    } catch (e: IOException) {
//        Log.e("App", "Failed to read file: ${e.message}")
//    }
//
//    return  fileSize
//}
//const val BUFFER_SIZE = 8192
////////////////////////////////////////////////////-----------------
//suspend fun Client(context: Context, device: InetAddress?, activity: MainActivity) {
//
//    val directory = activity.getOutputDirectory()
//
//    withContext(Dispatchers.IO) {
//        try {
//            // Connect to server
//            Log.d("App", "Client connecting...")
//            val clientSocket = Socket(device, 37682)
//            Log.d("App", "Client connected")
//
//
//            val dataInputStream = DataInputStream(clientSocket.getInputStream())
//
//            val dataOutputStream = DataOutputStream(clientSocket.getOutputStream())
//
//            // Receive the number of files from the server
//            val numFiles = dataInputStream.readInt()
//            Log.d("App", "Number of files = $numFiles")
//
//            // Receive each file
//            for (i in 0 until numFiles) {
//                // Receive the length of the file name
//                println("loop run for = $i")
//
//                println("data  stream = "+dataInputStream.available())
//
//                // Receive the file name
//                val fileNameLength = dataInputStream.readInt()
//                val fileNameBytes = ByteArray(fileNameLength)
//                dataInputStream.readFully(fileNameBytes)
//                val fileName = String(fileNameBytes)
//                Log.d("App", "File name = $fileName")
//
//                // Receive the file data from the server
//                try {
//
//                    val fileSize = byteArrayToUri(context.contentResolver, directory, fileName, clientSocket.getInputStream()).toDouble()/1024/1024
//                    Log.d("App", "File received: $fileSize MB")
//                    // giving server confirmation
//                    dataOutputStream.writeBoolean(true)
//
//                }catch (e:IOException){
//                   Log.e("App","could not save file : $fileName")
//                    dataOutputStream.writeBoolean(false)
//                }
//
//            }
//            // Close the connection
//            clientSocket.close()
//        } catch (e: IOException) {
//            Log.e("App", "Failed to connect to server: ${e.message}")
//        }
//    }
//}
//
//
//private fun receiveByteArray(inputStream: InputStream): ByteArray {
//    val dataInputStream = DataInputStream(inputStream)
//    val byteArraySize = dataInputStream.readInt()
//    val byteArray = ByteArray(byteArraySize)
//    dataInputStream.readFully(byteArray)
//    dataInputStream.close()
//    return byteArray
//}
//
//private fun byteArrayToUri(contentResolver: ContentResolver, directory: File, fileName: String, inputStream: InputStream): Long {
//    val file = File(directory, fileName)
//    val outputStream = FileOutputStream(file)
//    var fileSize = 0L
//
//    try {
//        var byteArray = receiveByteArray(inputStream)
//        while (byteArray.isNotEmpty()) {
//            outputStream.write(byteArray)
//            fileSize += byteArray.size
//            byteArray = receiveByteArray(inputStream)
//        }
//
//        Log.d("App", "File saved: ${file.absolutePath}, $fileSize bytes")
//    } catch (e: IOException) {
//        Log.e("App", "Failed to save file: ${e.message}")
//    }
//
//    return fileSize
//}



