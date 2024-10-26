package com.tifd.projectcomposed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tifd.projectcomposed.data.TugasEntity
import com.tifd.projectcomposed.viewmodel.TugasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasScreen() {
    val viewModel: TugasViewModel = viewModel()
    val tugas = viewModel.tugas.collectAsState().value
    var namaMatkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Input fields
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

        // Add button
        Button(
            onClick = {
                if (namaMatkul.isNotBlank() && detailTugas.isNotBlank()) {
                    viewModel.addTugas(namaMatkul, detailTugas)
                    namaMatkul = ""
                    detailTugas = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add")
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
                    onToggleDone = { viewModel.toggleTugasDone(task) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasItem(
    tugas: TugasEntity,
    onToggleDone: () -> Unit
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