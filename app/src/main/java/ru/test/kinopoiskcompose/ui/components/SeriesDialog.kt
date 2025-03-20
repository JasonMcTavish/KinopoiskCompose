package ru.test.kinopoiskcompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.test.kinopoiskcompose.db.model.SeasonEpisode

@Composable
fun SeriesDialog(
    onDismiss: () -> Unit,
    episodes: List<SeasonEpisode>
) {
    var selectedSeason by remember { mutableIntStateOf(1) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Сезоны и серии",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.White
                        )
                    }
                }

                val seasons = episodes
                    .map { it.seriesNumber }
                    .distinct()
                    .sorted()

                ScrollableTabRow(
                    selectedTabIndex = seasons.indexOf(selectedSeason),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    edgePadding = 16.dp
                ) {
                    seasons.forEach { season ->
                        Tab(
                            selected = selectedSeason == season,
                            onClick = { selectedSeason = season },
                            text = {
                                Text(
                                    text = season.toString(),
                                    color = if (selectedSeason == season)
                                        Color.White
                                    else
                                        Color.White.copy(alpha = 0.6f)
                                )
                            }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val seasonEpisodes = episodes
                        .filter { it.seriesNumber == selectedSeason }
                        .sortedBy { it.episodeNumber }

                    items(seasonEpisodes) { episode ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "${episode.episodeNumber} серия. ${episode.name}",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            if (!episode.synopsis.isNullOrEmpty()) {
                                Text(
                                    text = episode.synopsis,
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 