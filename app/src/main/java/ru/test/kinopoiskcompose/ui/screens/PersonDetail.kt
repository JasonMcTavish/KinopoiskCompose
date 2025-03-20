package ru.test.kinopoiskcompose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.test.kinopoiskcompose.ui.StateLoading
import ru.test.kinopoiskcompose.ui.components.FilmItem
import ru.test.kinopoiskcompose.ui.components.FilmographyDialog
import ru.test.kinopoiskcompose.ui.viewmodel.PersonDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    id: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PersonDetailViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val person by viewModel.currentPerson.collectAsState()
    val films by viewModel.films.collectAsState()
    val loadCurrentStaff by viewModel.loadCurrentStaff.collectAsState(initial = StateLoading.Loading)
    var showFilmography by remember { mutableStateOf(false) }
    val filters by viewModel.filters.collectAsState()
    val filmsWithFilter by viewModel.filmsWithFilter.collectAsState()

    LaunchedEffect(key1 = id) {
        viewModel.clearPersonData()
        val personId = id.toIntOrNull()
        if (personId != null) {
            viewModel.getPersonDetail(personId)
        }
    }

    if (showFilmography) {
        FilmographyDialog(
            onDismiss = { showFilmography = false },
            person = person?.personFilms?.name ?: "",
            onFilmClick = { filmId ->
                showFilmography = false
                navController.navigate("film/$filmId")
            },
            filters = filters,
            films = filmsWithFilter,
            onSelectFilter = { filter -> viewModel.setFilmsWithFilter(filter) }
        )
    }

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar(title = { }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
        }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
        )
    }) { paddingValues ->
        when (loadCurrentStaff) {
            StateLoading.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }

            is StateLoading.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "(loadCurrentStaff as StateLoading.Error).message",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            is StateLoading.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Фото актера
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        AsyncImage(
                            model = person?.personFilms?.poster,
                            contentDescription = person?.personFilms?.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Имя актера внизу фото
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = person?.personFilms?.name ?: "",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = person?.personFilms?.profession ?: "",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Секция "Лучшее"
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Лучшее",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(films.take(10)) { film ->
                                FilmItem(
                                    onItemClick = { navController.navigate("film/${film.filmId}") },
                                    imageUrl = film.poster,
                                    title = film.name,
                                    rating = film.rating ?: "",
                                    modifier = modifier
                                )
                            }
                        }
                    }

                    // Секция "Фильмография"
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Фильмография",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(text = "К списку",
                                fontSize = 16.sp,
                                color = Color.Blue,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable {
                                        showFilmography = true
                                        viewModel.setProfessionList()
                                    })
                        }

                        Text(
                            text = "${person?.films?.size ?: 0} ${getFilmsCountText(person?.films?.size ?: 0)}",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            StateLoading.Default -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun getFilmsCountText(count: Int): String {
    return when {
        count % 100 in 11..14 -> "фильмов"
        count % 10 == 1 -> "фильм"
        count % 10 in 2..4 -> "фильма"
        else -> "фильмов"
    }
}