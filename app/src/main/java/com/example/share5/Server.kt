package com.example.share5


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

//@RequiresApi(Build.VERSION_CODES.M)
//suspend fun Server(context: Context, uris: List<Uri>, fileNames: List<String>, activity: MainActivity) {
//    withContext(Dispatchers.IO) {
//        try {
//            // Create server socket
//            Log.d("App", "Server on")
//            val serverSocket = ServerSocket(37682)
//            serverSocket.soTimeout=10000
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
//            // Send the files names to the client
//            val outputString = fileNames.joinToString(separator = "\n")
//            dataOutputStream.writeInt(outputString.length)
//            dataOutputStream.writeBytes(outputString)
//            dataOutputStream.flush()
//
//            // Send each file
//            var i =0
//            for (uri in uris) {
//               // val fl = uriToFileSize(context.contentResolver,uri)
////                if(i>0){
////                    outputStream.write("\n".toByteArray())
////                }
//                val fileSize = uriToByteArray(context.contentResolver, uri, outputStream,activity)//,fl)
//                Log.d("App", "File sent: $fileSize bytes")
//               // i +=1
//            }
//
//            // Close the connection
//            clientSocket.close()
//            serverSocket.close()
//        } catch (e: IOException) {
//            Log.e("App", "Failed to start server: ${e.message}")
//            try {
//                val serverSocket = ServerSocket(37682)
//                serverSocket.close()
//            } catch (e: IOException) {
//                Log.d("App"," server = ${e.printStackTrace()}")
//            }
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.M)
suspend fun Server(context: Context, uris: List<Uri>, fileNames: List<String>, activity: MainActivity) {
    withContext(Dispatchers.IO) {
        try {
            // Create server socket
            Log.d("App", "Server on")
            val serverSocket = ServerSocket(37682)
            serverSocket.soTimeout=10000

            // Wait for a client to connect
            val clientSocket = serverSocket.accept()
            Log.d("App", "Client connected")

            // Send the number of files to the client.
            val numFiles = uris.size
            val outputStream = clientSocket.getOutputStream()
            val dataOutputStream = DataOutputStream(outputStream)
            dataOutputStream.writeInt(numFiles)

            //-----------------
            // Send each file
            for (i in uris.indices) {
                val uri = uris[i]
                val fileName = fileNames[i]
                Log.d("App","file name  = $fileName")
                // Send the file name and size

                val fileSize = uriToByteArray(context.contentResolver, uri, outputStream, activity)
                dataOutputStream.writeInt(fileName.length)
                dataOutputStream.writeBytes(fileName)
                dataOutputStream.writeLong(fileSize)

                // Send the file contents
               // uriToByteArray(context.contentResolver, uri, outputStream, activity)

                // Send the separator between the files
                if (i < uris.size - 1) {
                    dataOutputStream.writeInt(0)
                    dataOutputStream.writeBytes("\n")
                }
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
    dataOutputStream.flush()
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
    finally {
        try {
            inputStream?.close()
        } catch (e: IOException) {
            Log.e("App", "Failed to close input stream: ${e.message}")
        }
    }
 return  fileSize
}
private const val BUFFER_SIZE = 8192





//private fun uriToFileSize(contentResolver: ContentResolver, uri: Uri): Long {
//    var fileSize = 0L
//    var inputStream: InputStream? = null
//
//    try {
//        inputStream = contentResolver.openInputStream(uri)
//        fileSize = inputStream?.available()?.toLong() ?: 0L
//    } catch (e: IOException) {
//        Log.e("App", "Failed to get file size: ${e.message}")
//    } finally {
//        try {
//            inputStream?.close()
//        } catch (e: IOException) {
//            Log.e("App", "Failed to close input stream: ${e.message}")
//        }
//    }
//
//    return fileSize
//}

//
//private fun sendByteArray(outputStream: OutputStream, byteArray: ByteArray) {//, fileSize: Long) {
//    val dataOutputStream = DataOutputStream(outputStream)
//   // dataOutputStream.writeLong(fileSize)
//    dataOutputStream.writeInt(byteArray.size)
//    dataOutputStream.write(byteArray)
//    dataOutputStream.flush()
//}

//@RequiresApi(Build.VERSION_CODES.M)
//suspend fun Server(context: Context, uris: List<Uri>, fileNames: List<String>, activity: MainActivity) {
//    withContext(Dispatchers.IO) {
//        try {
//            // Create server socket
//            Log.d("App", "Server on")
//            val serverSocket = ServerSocket(37682)
//            serverSocket.soTimeout=10000
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
//
//            //-----------------
//            // Send each file
//            for (i in uris.indices) {
//                val uri = uris[i]
//                val fileName = fileNames[i]
//                Log.d("App","file name  = $fileName")
//                // Send the file name and size
//                val fileSize = uriToByteArray(context.contentResolver, uri, outputStream, activity)
//                dataOutputStream.writeInt(fileName.length)
//                dataOutputStream.writeBytes(fileName)
//                dataOutputStream.writeLong(fileSize)
//
//                // Send the file contents
//                uriToByteArray(context.contentResolver, uri, outputStream, activity)
//
//                // Send the separator between the files
//                if (i < uris.size - 1) {
//                    dataOutputStream.writeInt(0)
//                    dataOutputStream.writeBytes("\n")
//                }
//            }
//
//
//            // Close the connection
//            clientSocket.close()
//            serverSocket.close()
//        } catch (e: IOException) {
//            Log.e("App", "Failed to start server: ${e.message}")
//            try {
//                val serverSocket = ServerSocket(37682)
//                val clientSocket = serverSocket.accept()
//                serverSocket.close()
//                clientSocket.close()
//            } catch (e: IOException) {
//                Log.d("App"," server = ${e.printStackTrace()}")
//            }
//        }
//    }
//}

