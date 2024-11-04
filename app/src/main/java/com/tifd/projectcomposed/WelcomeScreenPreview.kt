package com.tifd.projectcomposed

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProjectComposeDTheme {
        WelcomeScreen(
            onLoginSuccess = {},
            context = LocalContext.current
        )
    }
}
