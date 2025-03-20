package ru.test.kinopoiskcompose.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    data object HomeScreen: BottomNavigationItems(
        route = "home_screen",
        title = "Главная",
        icon = Icons.Outlined.Home
    )

    data object SearchScreen: BottomNavigationItems(
        route = "search_screen",
        title = "Поиск",
        icon = Icons.Outlined.Search
    )

    data object ProfileScreen: BottomNavigationItems(
        route = "profile_Screen",
        title = "Профиль",
        icon = Icons.Outlined.Person
    )
}