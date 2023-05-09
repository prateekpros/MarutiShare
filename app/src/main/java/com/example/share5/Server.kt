package com.example.share5


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

//----------
//@RequiresApi(Build.VERSION_CODES.M)
//suspend fun Server(context: Context, uris: List<Uri>, fileNames: List<String>, activity: MainActivity) {
//    withContext(Dispatchers.IO) {
//        try {
//            // Create server socket
//            Log.d("App", "Server on")
//            val serverSocket = ServerSocket(37682)
//            //serverSocket.soTimeout=500000
//
//            // Wait for a client to connect
//            val clientSocket = serverSocket.accept()
//            Log.d("App", "Client connected")
//
//            // Send the number of files to the client.
//            val numFiles = uris.size
//            val outputStream = clientSocket.getOutputStream()
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
//                dataOutputStream.flush()
//
//                // Send the file contents
//                uriToByteArray(context.contentResolver, uri, outputStream, activity)
//
//                // Send the separator between the files
//                if (i < uris.size ) {
//                    dataOutputStream.writeBytes("\n")
//                }
//
//            }
//
//            // Close the connection
//            clientSocket.close()
//            serverSocket.close()
//        } catch (e: IOException) {
//            Log.e("App", "Failed to start server: ${e.message}")
//
//        }
//    }
//}
///////////////////////////////////////

@RequiresApi(Build.VERSION_CODES.M)
suspend fun Server(context: Context, uris: List<Uri>, fileNames: List<String>, activity: MainActivity) {
    withContext(Dispatchers.IO) {
        try {
            // Create server socket
            Log.d("App", "Server on")
            val serverSocket = ServerSocket(37682)
            //serverSocket.soTimeout=500000

            // Wait for a client to connect
            val clientSocket = serverSocket.accept()
            Log.d("App", "Client connected")

            // Send the number of files to the client.
            val numFiles = uris.size
            val outputStream = clientSocket.getOutputStream()
            val inputStream = clientSocket.getInputStream()
            val dataInputStream = DataInputStream(inputStream)
            val dataOutputStream = DataOutputStream(outputStream)
            dataOutputStream.writeInt(numFiles)

            //-----------------
            // Send each file
            for (i in uris.indices) {
                val uri = uris[i]
                val fileName = fileNames[i]
                Log.d("App","file name  = $fileName")

                // Send the file name and size
                dataOutputStream.writeInt(fileName.length)
                dataOutputStream.writeBytes(fileName)
                val inputStream = context.contentResolver.openInputStream(uri)
                val size = inputStream?.available()?.toLong() ?: -1
                inputStream?.close()
                dataOutputStream.writeLong(size)

                // Send the file contents
                uriToByteArray(context.contentResolver, uri, outputStream, activity)
                dataOutputStream.flush()

            }


            // Close the connection
            clientSocket.close()
            serverSocket.close()
        } catch (e: IOException) {
            Log.e("App", "Failed to start server: ${e.message}")
        }
    }
}


private fun sendByteArray(outputStream: OutputStream, byteArray: ByteArray) {
    val dataOutputStream = DataOutputStream(outputStream)
    dataOutputStream.writeInt(byteArray.size)
    dataOutputStream.write(byteArray)

}


@RequiresApi(Build.VERSION_CODES.M)
private fun uriToByteArray(contentResolver: ContentResolver, uri: Uri, outputStream: OutputStream, activity: MainActivity):Long{
    var fileSize = 0L
    var inputStream: InputStream? = null


    try {
        inputStream = contentResolver.openInputStream(uri)
        val buffer = ByteArray(BUFFER_SIZE)
        var bytesRead: Int

        if (inputStream != null) {

            var totalBytesRead = 0L

             val startTime = System.currentTimeMillis()

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                totalBytesRead += bytesRead
                sendByteArray(outputStream, buffer.copyOf(bytesRead))
                fileSize += bytesRead
                val endTime = System.currentTimeMillis()
                val timeTaken = (endTime - startTime) / 1000.0 // in seconds
                if (totalBytesRead > 0) {
                    val speed = totalBytesRead / (timeTaken*1000000) // in bytes per second
                    println("Transfer speed: $speed MB/second")
                }
            }

        }


        Log.d("App", "File read: $fileSize bytes")
    } catch (e: IOException) {
        Log.e("App", "Failed to read file: ${e.message}")
    }


 return  fileSize
}
const val BUFFER_SIZE = 8192

/////////////////////////////////////////------------------------------------------------------

//@RequiresApi(android.os.Build.VERSION_CODES.M)
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
//                Log.d("App", "file name  = $fileName")
//                // Send the file name and size
//                dataOutputStream.writeInt(fileName.length)
//                dataOutputStream.writeBytes(fileName)
//
//                // Send the file contents
//                uriToByteArray(context.contentResolver, uri, outputStream, activity)
//
////                while(dataInputStream.available() == 0)
////                {
////                    delay(500)
////                }
//
//                if(dataInputStream.readBoolean()) {
//                    Log.d("App","file : $fileName received by client")
//                }
//                else{
//                    Log.d("App","file : $fileName is not received by client")
//                }
//                dataOutputStream.flush()
//
//            }
//
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
//    dataOutputStream.close()
//
//}
//
//
//
//@RequiresApi(Build.VERSION_CODES.M)
//private fun uriToByteArray(contentResolver: ContentResolver, uri: Uri, outputStream: OutputStream, activity: MainActivity){
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
//
//
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                totalBytesRead += bytesRead
//                sendByteArray(outputStream, buffer.copyOf(bytesRead))
//                fileSize += bytesRead
//            }
//
//        }
//        inputStream?.close()
//
//        Log.d("App", "File Sent : $fileSize bytes")
//    } catch (e: IOException) {
//        Log.e("App", "Failed to send file: ${e.message}")
//    }
//
//}
//const val BUFFER_SIZE = 8192
