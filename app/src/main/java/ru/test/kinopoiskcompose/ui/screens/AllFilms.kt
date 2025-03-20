package ru.test.kinopoiskcompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.test.kinopoiskcompose.data.CategoriesFilms
import ru.test.kinopoiskcompose.model.FilmWithGenresModel
import ru.test.kinopoiskcompose.ui.components.FilmItem
import ru.test.kinopoiskcompose.ui.viewmodel.AllFilmsViewModel

@Composable
fun AllFilmsScreen(
    categoryName: String,
    navController: NavController,
    viewModel: AllFilmsViewModel = hiltViewModel()
) {
    val category: CategoriesFilms = when (categoryName) {
        "BEST" -> CategoriesFilms.BEST
        "POPULAR" -> CategoriesFilms.POPULAR
        "PREMIERS" -> CategoriesFilms.PREMIERS
        "AWAIT" -> CategoriesFilms.AWAIT
        "TV_SERIES" -> CategoriesFilms.TV_SERIES

        else -> {
            CategoriesFilms.AWAIT
        }
    }
    LaunchedEffect(categoryName) {
        viewModel.setCurrentCategory(category)
    }
    val films = viewModel.allFilmsByCategory.collectAsLazyPagingItems()

    AllFilmsContent(categoryName,
        films,
        onItemClick = { filmId ->
            navController.navigate("film/${filmId}")
        },
        onRetryClick = { viewModel.setCurrentCategory(category) },
        onBack = { navController.navigateUp() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFilmsContent(
    categoryName: String,
    films: LazyPagingItems<FilmWithGenresModel>,
    onItemClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit
) {
    val state = films.loadState
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = categoryName) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                state.refresh is LoadState.Error -> {
                    Text(
                        "Ошибка загрузки",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(films.itemCount) { index ->
                            films[index]?.let { film ->
                                FilmItem(
                                    imageUrl = film.film.poster,
                                    title = film.film.name,
                                    rating = film.film.rating ?: "",
                                    onItemClick = {
                                        onItemClick(film.film.filmId.toString())
                                    }
                                )
                            }
                        }

                        if (state.append is LoadState.Loading) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


