package ru.test.kinopoiskcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.test.kinopoiskcompose.R
import ru.test.kinopoiskcompose.app.getStrCountriesLengthAge
import ru.test.kinopoiskcompose.data.PROFESSIONS
import ru.test.kinopoiskcompose.db.model.FilmPersons
import ru.test.kinopoiskcompose.ui.StateLoading
import ru.test.kinopoiskcompose.ui.components.GallerySection
import ru.test.kinopoiskcompose.ui.components.Person
import ru.test.kinopoiskcompose.ui.components.PersonList
import ru.test.kinopoiskcompose.ui.components.Rating
import ru.test.kinopoiskcompose.ui.components.SeriesDialog
import ru.test.kinopoiskcompose.ui.components.SimilarFilms
import ru.test.kinopoiskcompose.ui.components.StaffDialog
import ru.test.kinopoiskcompose.ui.viewmodel.FilmDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FilmDetailViewModel = hiltViewModel()
) {
    var showEpisodesDialog by remember { mutableStateOf(false) }
    var showActorsDialog by remember { mutableStateOf(false) }
    var showCrewDialog by remember { mutableStateOf(false) }

    val filmId = id.toIntOrNull() ?: 1
    val film by viewModel.filmDetailInfo.collectAsState()
    val loadingState by viewModel.loadCurrentFilmState.collectAsState()

    var isFavorite by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    LaunchedEffect(filmId) {
        viewModel.clearFilmData()
        viewModel.getFilmById(filmId)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Поделиться */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Поделиться",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* Дополнительные действия */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Ещё",
                            tint = Color.White
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        when (loadingState) {
            is StateLoading.Success -> {
                film?.let { currentFilm ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(currentFilm.film.poster)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = currentFilm.film.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Градиент поверх изображения
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.7f),
                                                Color.Black
                                            ),
                                            startY = 200f
                                        )
                                    )
                            )

                            // Информация о фильме поверх градиента
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = currentFilm.film.name,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Rating(text = currentFilm.film.rating ?: "")
                                    Text(
                                        text = "${currentFilm.detailInfo?.year ?: ""}, ${currentFilm.genres.firstOrNull()?.genre ?: ""}, ${currentFilm!!.getStrCountriesLengthAge()}",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    IconButton(
                                        onClick = { isFavorite = !isFavorite }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = "Нравится",
                                            tint = if (isFavorite) Color.Blue else Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { isBookmarked = !isBookmarked }
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_bookmark),
                                            contentDescription = "Смотреть позже",
                                            tint = if (isBookmarked) Color.Blue else Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Описание фильма
                        Text(
                            text = currentFilm.detailInfo?.description ?: "",
                            modifier = Modifier.padding(16.dp),
                            color = Color.White,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Проверка и отображение сезонов и серий
                        if (!currentFilm.seriesEpisodes.isNullOrEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Сезоны и серии",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Text(
                                        text = "Все",
                                        fontSize = 16.sp,
                                        color = Color.Blue,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .clickable { showEpisodesDialog = true }
                                    )
                                }

                                val totalSeasons =
                                    currentFilm.seriesEpisodes.distinctBy { it.seriesNumber }.size
                                val totalEpisodes = currentFilm.seriesEpisodes.size

                                Text(
                                    text = pluralStringResource(
                                        R.plurals.seasons_count,
                                        totalSeasons,
                                        totalSeasons
                                    ) + ", " +
                                            pluralStringResource(
                                                R.plurals.episodes_count,
                                                totalEpisodes,
                                                totalEpisodes
                                            ),
                                    fontSize = 16.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            if (showEpisodesDialog) {
                                SeriesDialog(
                                    onDismiss = { showEpisodesDialog = false },
                                    episodes = currentFilm.seriesEpisodes
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }


                        val actors = mutableListOf<FilmPersons>()
                        val crew = mutableListOf<FilmPersons>()

                        currentFilm.persons?.forEach {
                            if (it.professionKey == "ACTOR") {
                                actors.add(it)
                            } else {
                                crew.add(it)
                            }
                        }
                        // Список актёров
                        PersonList(
                            title = "В фильме снимались",
                            people = actors.map { actor ->
                                Person(
                                    id = actor.personId,
                                    name = actor.name,
                                    photo = actor.poster,
                                    role = PROFESSIONS.getValue(actor.professionKey)
                                )
                            },
                            onItemClick = { personId -> navController.navigate("person/$personId") },
                            onShowAllClick = { showActorsDialog = true }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Создатели фильма
                        PersonList(
                            title = "Над фильмом работали",
                            people = crew.map { person ->
                                Person(
                                    id = person.personId,
                                    name = person.name,
                                    photo = person.poster,
                                    role = PROFESSIONS.getValue(person.professionKey)
                                )
                            },
                            onItemClick = { personId -> navController.navigate("person/$personId") },
                            onShowAllClick = { showCrewDialog = true }
                        )

                        if (showActorsDialog) {
                            StaffDialog(
                                title = "В фильме снимались",
                                onDismiss = { showActorsDialog = false },
                                onPersonClick = { personId -> navController.navigate("person/$personId") },
                                personList = actors
                            )
                        }

                        if (showCrewDialog) {
                            StaffDialog(
                                title = "Над фильмом работали",
                                onDismiss = { showCrewDialog = false },
                                onPersonClick = { personId -> navController.navigate("person/$personId") },
                                personList = crew
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Галерея
                        if (currentFilm.gallery.isNotEmpty()) {
                            GallerySection(
                                title = "Галерея",
                                images = currentFilm.gallery,
                                onClickAll = { navController.navigate("gallery/${currentFilm.film.filmId}") }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Похожие фильмы
                        if (currentFilm.similar?.isNotEmpty() == true) {
                            SimilarFilms(
                                films = currentFilm.similar,
                                onFilmClick = { filmId ->
                                    navController.navigate("film/${filmId}") {
                                        popUpTo("film/${id}") {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            is StateLoading.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Произошла ошибка при загрузке",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}