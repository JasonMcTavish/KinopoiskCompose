package ru.test.kinopoiskcompose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import ru.test.kinopoiskcompose.ui.StateLoading
import ru.test.kinopoiskcompose.ui.components.BottomSheetShowImage
import ru.test.kinopoiskcompose.ui.viewmodel.GalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    id: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    LaunchedEffect(id) {
        viewModel.setGallery(id.toIntOrNull() ?: 1)
    }

    val images by viewModel.images.collectAsState()
    val loadGalleryState by viewModel.loadGalleryState.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val imageFullScreen by viewModel.imageFullScreen.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var selectedFilter by remember { mutableStateOf("") }

    LaunchedEffect(filters) {
        if (filters.isNotEmpty() && selectedFilter.isEmpty()) {
            selectedFilter = filters.first()
            viewModel.onSelectFilter(selectedFilter)
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Галерея", color = Color.White) }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
        )
    }) { paddingValues ->
        when (loadGalleryState) {
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
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    // Фильтры
                    LazyRow(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filters) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = {
                                    selectedFilter = filter
                                    viewModel.onSelectFilter(filter)
                                },
                                label = { Text(filter) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF1F1F1F),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.Transparent,
                                    labelColor = Color.White
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true, selected = selectedFilter == filter
                                )
                            )
                        }
                    }

                    // Сетка галереи
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(images) { image ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(image.preview)
                                    .crossfade(true).build(),
                                contentDescription = image.imageCategory,
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.setImageToFullScreen(image)
                                        scope.launch { sheetState.show() }
                                    }
                            )
                        }
                    }

                    if (sheetState.isVisible && imageFullScreen != null) {
                        BottomSheetShowImage(
                            image = imageFullScreen!!,
                            sheetState = sheetState,
                            onDismiss = { scope.launch { sheetState.hide() } }
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