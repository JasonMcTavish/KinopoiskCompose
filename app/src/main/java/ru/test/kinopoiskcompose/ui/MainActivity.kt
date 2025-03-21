package ru.test.kinopoiskcompose.ui

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.test.kinopoiskcompose.ui.navigation.NavigationGraph
import ru.test.kinopoiskcompose.ui.theme.KinopoiskComposeTheme
import androidx.core.content.edit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var startDestination = "home_screen"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val getPrefs = PreferenceManager.getDefaultSharedPreferences(baseContext)

        val FIRST_START = null
        val isFirstStart = getPrefs.getBoolean(FIRST_START, true)

        if (isFirstStart) {
            startDestination = "intro"
            getPrefs.edit() {
                putBoolean(FIRST_START, false)
            }
        } else {
            startDestination = "home_screen"
        }
        setContent {
            KinopoiskComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(modifier = Modifier, startDestination)
                }
            }
        }
    }

    @Composable
    fun MainScreen(modifier: Modifier, startDestination: String) {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                MyBottomAppBar(navController = navController)
            }
        ) { innerPadding ->
            Box(
                modifier = modifier.padding(
                    PaddingValues(0.dp, 0.dp, 0.dp, innerPadding.calculateBottomPadding())
                )
            ) {
                NavigationGraph(modifier = modifier, navController, startDestination)
            }
        }
    }
}

