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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

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

    LaunchedEffect(Unit) {
        firestore.collection("schedules")
            .get()
            .addOnSuccessListener { result ->
                scheduleItems = result.toObjects(ScheduleItem::class.java)
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Your Schedule",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(scheduleItems) { item ->
                ScheduleItemCard(item)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleItemCard(item: ScheduleItem) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.mataKuliah, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${item.hari}, ${item.jamMulai} - ${item.jamSelesai}", fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = item.kelas, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Dosen: ${item.dosen}", fontSize = 14.sp)
            Text(text = "Ruang: ${item.ruang}", fontSize = 14.sp)
        }
    }
}

