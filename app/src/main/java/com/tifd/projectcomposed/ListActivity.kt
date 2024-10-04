package com.tifd.projectcomposed

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScheduleScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Annotate for using experimental APIs
@Composable
fun ScheduleScreen() {
    var scheduleItems by remember { mutableStateOf(emptyList<ScheduleItem>()) }
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // Load data from Firestore
    LaunchedEffect(Unit) {
        firestore.collection("schedules")
            .get()
            .addOnSuccessListener { result ->
                scheduleItems = result.toObjects(ScheduleItem::class.java)
            }
            .addOnFailureListener { exception ->
                Log.e("ScheduleScreen", "Error getting documents: $exception")
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Schedule") },
                actions = {
                    // Top button (GitHub Profile)
                    IconButton(onClick = {
                        try {
                            val intent = Intent(context, GithubProfileActivity::class.java)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("ScheduleScreen", "Error starting GitHub profile activity", e)
                        }
                    }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "GitHub Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            // Bottom button (GitHub Profile)
            FloatingActionButton(onClick = {
                try {
                    val intent = Intent(context, com.tifd.projectcomposed.GithubProfileActivity::class.java)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("ScheduleScreen", "Error starting GitHub profile activity from FAB", e)
                }
            }) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "GitHub Profile")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(scheduleItems) { item ->
                ScheduleItemCard(item)
            }
        }
    }
}

@Composable
fun ScheduleItemCard(item: ScheduleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.mataKuliah, fontWeight = FontWeight.Bold)
            Text(text = "Hari, Tanggal: ${item.hari}, ${item.jamMulai} - ${item.jamSelesai}")
            Text(text = "Kode: ${item.kode}")
            Text(text = "Kelas: ${item.kelas}")
            Text(text = "Dosen: ${item.dosen}")
            Text(text = "Ruang: ${item.ruang}")
        }
    }
}

