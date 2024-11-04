package com.tifd.projectcomposed

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext  // Add this line
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.tifd.projectcomposed.data.TugasEntity
import com.tifd.projectcomposed.viewmodel.TugasViewModel
import java.io.File
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Text
import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasScreen() {
    val viewModel: TugasViewModel = viewModel()
    val tugas = viewModel.tugas.collectAsState().value
    var namaMatkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDialog by remember { mutableStateOf(false) } // To control dialog visibility
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) } // Track clicked image URI

    // Context and URI for file
    val context = LocalContext.current
    val photoFile = File(context.cacheDir, "tugas_image.jpg")
    val photoUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)

    // Launchers for Gallery and Camera
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) imageUri = photoUri
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Input fields and image preview
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

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add")
            }

            IconButton(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(photoUri)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Icon(Icons.Default.Camera, contentDescription = "Camera")
            }

            IconButton(onClick = {
                galleryLauncher.launch("image/*")
            }) {
                Icon(Icons.Default.Camera, contentDescription = "Gallery")
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

    // Image dialog to show full-screen image
    if (showImageDialog && selectedImageUri != null) {
        Dialog(onDismissRequest = { showImageDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { showImageDialog = false }, // Close on click
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
                Text(
                    text = tugas.namaMatkul,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = tugas.detailTugas,
                    style = MaterialTheme.typography.bodyMedium
                )
                // Display image if available
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
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Done",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                OutlinedButton(onClick = onToggleDone) {
                    Text("Is Done")
                }
            }
        }
    }
}

