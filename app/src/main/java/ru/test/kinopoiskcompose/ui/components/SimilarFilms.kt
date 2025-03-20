package ru.test.kinopoiskcompose.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.test.kinopoiskcompose.db.model.FilmSimilar

@Composable
fun SimilarFilms(
    films: List<FilmSimilar>,
    onFilmClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Похожие фильмы",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.padding(start = 16.dp)) }

            items(films) { film ->
                FilmItem(
                    imageUrl = film.poster,
                    title = film.name ?: "",
                    rating =  "",
                    onItemClick = { onFilmClick(film.similarId) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
} 