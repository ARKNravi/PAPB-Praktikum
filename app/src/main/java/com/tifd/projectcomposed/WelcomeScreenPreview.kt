package com.tifd.projectcomposed

import WelcomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProjectComposeDTheme {
        WelcomeScreen()
    }
}