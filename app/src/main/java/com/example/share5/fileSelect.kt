package com.example.share5


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//App(selectedFiles , add ={
//    selectedFiles += it
//    val temp = selectedFiles.toSet()
//    selectedFiles =temp.toList() },minus = {
//    selectedFiles -= it
//})

@Composable
fun App(selectedFiles: List<Uri>, add:(List<Uri>)->Unit, minus:(Uri)->Unit){

    Column() {
        FilePicker(onFilesSelected = { uris ->
            add(uris)
        })
        var names= emptySet<String>()

        if (selectedFiles.isNotEmpty()) {
            Log.d("App","$selectedFiles")
            names = SelectedFilesCard(selectedUris = selectedFiles, remove = {
                minus(it)
            }
            )
        }
    }
}

@Composable
fun FilePicker(onFilesSelected: (List<Uri>) -> Unit) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            onFilesSelected(uris)
        }
    Box(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .width(120.dp)
            .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = "#6f43fa".color,
                contentColor = Color.White
            ),
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1
                        )
                    } else {
                        launcher.launch(arrayOf("*/*"))
                    }
                } else {
                    launcher.launch(arrayOf("*/*"))
                }
            }
        ) {
            Text(text = "Select files")
        }

    }

}

@Composable
fun SelectedFilesCard(selectedUris: List<Uri>,remove:(Uri) ->Unit):Set<String> {
    var names = emptySet<String>()
    LazyColumn {
        itemsIndexed(selectedUris.toList()) { index ,uri ->
            Card(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color(172, 134, 246),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
                    .fillMaxWidth()

            ) {

               val name = getFileName(uri = uri)
                names +=name
                Box() {
                    IconButton(onClick = { remove(uri); names-=name}, modifier = Modifier.align(Alignment.CenterEnd)) {

                        Icon(imageVector = Icons.Default.Delete , contentDescription = null, modifier = Modifier.padding(5.dp))
                    }
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center
                        , horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = getFileName(uri = uri),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 8.dp), color = Color.Black
                        )
                    }
                }
            }
        }
    }
 return names
}




@Composable
fun getFileName(uri: Uri): String {
    val file = DocumentFile.fromSingleUri(LocalContext.current, uri)
    return if (file?.name == null)
        "no name"
    else
        file.name!!
}


