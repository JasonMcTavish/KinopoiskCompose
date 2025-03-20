package ru.test.kinopoiskcompose.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.map
import ru.test.kinopoiskcompose.ui.navigation.BottomNavigationItems

@Composable
fun MyBottomAppBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomNavigationItems.HomeScreen,
        BottomNavigationItems.SearchScreen,
        BottomNavigationItems.ProfileScreen
    )
    val currentRoute = navController.currentBackStackEntryFlow.map { backStackEntry ->
        backStackEntry.destination.route
    }.collectAsState(initial = "home_screen")

    var selectedItem by remember { mutableIntStateOf(0) }

    screens.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute.value) {
            selectedItem = index
        }
    }

    NavigationBar {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                alwaysShowLabel = true,
                label = { Text(text = screen.title!!) },
                icon = { Icon(imageVector = screen.icon!!, contentDescription = "") },
                selected = selectedItem == index, onClick = {
                    selectedItem = index
                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    indicatorColor = Color.White
                )

            )
        }
    }
}