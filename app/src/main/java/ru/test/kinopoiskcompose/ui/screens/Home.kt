package ru.test.kinopoiskcompose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.test.kinopoiskcompose.R
import ru.test.kinopoiskcompose.ui.StateLoading
import ru.test.kinopoiskcompose.ui.components.CategorySection
import ru.test.kinopoiskcompose.ui.components.HomeList
import ru.test.kinopoiskcompose.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.getFilmsByCategories()
    }

    val homePageList by viewModel.homePageList.collectAsState()
    val loadCategoryState by viewModel.loadCategoryState.collectAsState()

    HomeContent(
        modifier = modifier,
        categories = homePageList,
        loadingState = loadCategoryState,
        onFilmClick = { filmId -> 
            navController.navigate("film/${filmId}")
        },
        onShowAllClick = { navController.navigate("all_films/${it}") },
        onRetryClick = { viewModel.getFilmsByCategories() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    modifier: Modifier,
    categories: List<HomeList>,
    loadingState: StateLoading,
    onFilmClick: (Int) -> Unit,
    onShowAllClick: (String) -> Unit,
    onRetryClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (loadingState) {
                is StateLoading.Success -> {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        categories.forEach { category ->
                            CategorySection(
                                title = category.category.name,
                                films = category.filmList,
                                onShowAllClick = { onShowAllClick(category.category.name) },
                                onFilmClick = onFilmClick,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.padding(bottom = 16.dp))
                    }
                }

                is StateLoading.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Произошла ошибка при загрузке",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = onRetryClick,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(stringResource(R.string.btn_try_again))
                        }
                    }
                }
            }
        }
    }
}
