package ru.test.kinopoiskcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.test.kinopoiskcompose.ui.screens.AllFilmsScreen
import ru.test.kinopoiskcompose.ui.screens.FilmDetailScreen
import ru.test.kinopoiskcompose.ui.screens.GalleryScreen
import ru.test.kinopoiskcompose.ui.screens.HomeScreen
import ru.test.kinopoiskcompose.ui.screens.IntroScreen
import ru.test.kinopoiskcompose.ui.screens.PersonDetailScreen
import ru.test.kinopoiskcompose.ui.screens.ProfileScreen
import ru.test.kinopoiskcompose.ui.screens.SearchScreen


@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(route = "home_screen") {
            HomeScreen(modifier = modifier, navController = navController)
        }
        composable(route = "search_screen") {
            SearchScreen()
        }
        composable(route = "profile_Screen") {
            ProfileScreen()
        }
        composable(
            route = "all_films/{category}", arguments =
                listOf(navArgument(name = "category") {
                    type = NavType.StringType
                    nullable = false
                })
        ) {
            val category = it.arguments?.getString("category")
            if (category != null)
                AllFilmsScreen(categoryName = category, navController = navController)
        }
        composable(
            route = "film/{id}", arguments =
                listOf(navArgument(name = "id") {
                    type = NavType.StringType
                    nullable = false
                })
        ) {
            val id = it.arguments?.getString("id")
            if (id != null)
                FilmDetailScreen(id = id, modifier = modifier, navController = navController)
        }
        composable(
            route = "gallery/{id}", arguments =
                listOf(navArgument(name = "id") {
                    type = NavType.StringType
                    nullable = false
                })
        ) {
            val id = it.arguments?.getString("id")
            if (id != null)
                GalleryScreen(
                    id = id,
                    navController = navController,
                    modifier = modifier
                )
        }
        composable(
            route = "person/{id}", arguments =
                listOf(navArgument(name = "id") {
                    type = NavType.StringType
                    nullable = false
                })
        ) {
            val id = it.arguments?.getString("id")
            if (id != null)
                PersonDetailScreen(
                    id = id,
                    navController = navController,
                    modifier = modifier
                )
        }

        composable(route = "intro") {
            IntroScreen(modifier, navController)
        }

    }
}