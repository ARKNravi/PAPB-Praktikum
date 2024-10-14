package com.tifd.projectcomposed

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "matkul", // Start with "Matkul" screen
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("matkul") { ScheduleScreen() }
            composable("tugas") { TugasScreen() }
            composable("profile") { GithubProfileScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            label = { Text("Matkul") },
            icon = { Icon(Icons.Default.List, contentDescription = "Matkul") },
            selected = navController.currentDestination?.route == "matkul",
            onClick = { navController.navigate("matkul") }
        )
        NavigationBarItem(
            label = { Text("Tugas") },
            icon = { Icon(Icons.Default.Info, contentDescription = "Tugas") },
            selected = navController.currentDestination?.route == "tugas",
            onClick = { navController.navigate("tugas") }
        )
        NavigationBarItem(
            label = { Text("Profile") },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            selected = navController.currentDestination?.route == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}
