package com.tifd.projectcomposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class GithubProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubProfileScreen()
        }
    }
}

@Composable
fun GithubProfileScreen() {
    val scope = rememberCoroutineScope()
    var profilePic by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var followers by remember { mutableStateOf(0) }
    var following by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        scope.launch {
            fetchGithubData { result ->
                profilePic = result.getString("avatar_url")
                username = result.getString("login")
                name = result.getString("name")
                followers = result.getInt("followers")
                following = result.getInt("following")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (profilePic.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(profilePic),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(120.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = username, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Followers: $followers | Following: $following")
    }
}


fun fetchGithubData(onResult: (JSONObject) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.github.com/users/ARKNravi")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string() ?: return
            val jsonObject = JSONObject(responseBody)
            onResult(jsonObject)
        }
    })
}
