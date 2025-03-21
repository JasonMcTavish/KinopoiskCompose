package ru.test.kinopoiskcompose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun IntroScreen(modifier: Modifier, navController: NavController) {
    Scaffold() {
        Box(modifier = modifier.padding(paddingValues = it)) {
            Button(onClick = { navController.navigate("home_screen") }) { }
        }
    }
}