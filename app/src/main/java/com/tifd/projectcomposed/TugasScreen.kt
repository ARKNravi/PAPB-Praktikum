package com.tifd.projectcomposed

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.tifd.projectcomposed.data.TugasEntity
import com.tifd.projectcomposed.viewmodel.TugasViewModel
import java.io.File
import java.util.concurrent.Executors
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasScreen() {
    val viewModel: TugasViewModel = viewModel()
    val tugas = viewModel.tugas.collectAsState().value
    var namaMatkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) } // Track clicked image URI
    var showCamera by remember { mutableStateOf(false) }

    // Launcher for gallery selection
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = namaMatkul,
            onValueChange = { namaMatkul = it },
            label = { Text("Nama Matkul") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = detailTugas,
            onValueChange = { detailTugas = it },
            label = { Text("Detail Tugas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp)
                    .clickable {
                        selectedImageUri = uri // Set selected image URI
                        showImageDialog = true // Show dialog
                    }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (namaMatkul.isNotBlank() && detailTugas.isNotBlank()) {
                        viewModel.addTugas(namaMatkul, detailTugas, imageUri.toString())
                        namaMatkul = ""
                        detailTugas = ""
                        imageUri = null
                    }
                }
            ) {
                Text("Add")
            }

            IconButton(onClick = { galleryLauncher.launch("image/*") }) { // Launch gallery
                Icon(Icons.Default.Camera, contentDescription = "Gallery")
            }

            IconButton(onClick = { showCamera = true }) {
                Icon(Icons.Default.Camera, contentDescription = "Camera")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of tasks
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tugas) { task ->
                TugasItem(
                    tugas = task,
                    onToggleDone = { viewModel.toggleTugasDone(task) },
                    onImageClick = { uri ->
                        selectedImageUri = uri // Set selected image URI
                        showImageDialog = true // Show dialog
                    }
                )
            }
        }
    }

    if (showCamera) {
        CameraPreview(
            onCapture = { uri ->
                imageUri = uri
                showCamera = false
            },
            onClose = { showCamera = false }
        )
    }

    // Show full-screen image dialog if an image is clicked
    if (showImageDialog && selectedImageUri != null) {
        Dialog(onDismissRequest = { showImageDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showImageDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(selectedImageUri),
                    contentDescription = "Full View Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CameraPreview(onCapture: (Uri) -> Unit, onClose: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val outputDirectory = getOutputDirectory(context)
    var imageCapture: ImageCapture? = remember { null }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val previewView = PreviewView(context)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                imageCapture = ImageCapture.Builder().build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))

            previewView
        },
        update = {}
    )

    // Capture button
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        IconButton(
            onClick = {
                val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture?.takePicture(
                    outputOptions,
                    executor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            exc.printStackTrace()
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            onCapture(Uri.fromFile(photoFile))
                        }
                    }
                )
            }
        ) {
            Icon(Icons.Default.Camera, contentDescription = "Capture", tint = Color.White)
        }
    }
}

// Utility function to get directory
fun getOutputDirectory(context: Context): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, context.packageName).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
}

@Composable
fun TugasItem(
    tugas: TugasEntity,
    onToggleDone: () -> Unit,
    onImageClick: (Uri) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggleDone
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tugas.namaMatkul, style = MaterialTheme.typography.titleMedium)
                Text(text = tugas.detailTugas, style = MaterialTheme.typography.bodyMedium)
                tugas.imageUri?.let { uri ->
                    Image(
                        painter = rememberImagePainter(uri),
                        contentDescription = "Task Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(top = 8.dp)
                            .clickable {
                                onImageClick(Uri.parse(uri)) // Trigger image click
                            }
                    )
                }
            }
            if (tugas.isDone) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = MaterialTheme.colorScheme.primary)
            } else {
                OutlinedButton(onClick = onToggleDone) {
                    Text("Is Done")
                }
            }
        }
    }
}
