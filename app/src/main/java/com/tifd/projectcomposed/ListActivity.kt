package com.tifd.projectcomposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScheduleScreen()
        }
    }
}

@Composable
fun ScheduleScreen() {
    var scheduleItems by remember { mutableStateOf(emptyList<ScheduleItem>()) }
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        firestore.collection("schedules")
            .get()
            .addOnSuccessListener { result ->
                scheduleItems = result.toObjects(ScheduleItem::class.java)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(scheduleItems) { item ->
            ScheduleItemCard(item)
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
            Text(text = "Hari, Tanggal : ${item.hari}, ${item.jamMulai} - ${item.jamSelesai}")
            Text(text = "Kode: ${item.kode}")
            Text(text = "Kelas: ${item.kelas}")
            Text(text = "Dosen: ${item.dosen}")
            Text(text = "Ruang: ${item.ruang}")
        }
    }
}