package ru.test.kinopoiskcompose.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.test.kinopoiskcompose.db.model.FilmWithGenres

@Composable
fun CategorySection(
    title: String,
    films: List<FilmWithGenres>,
    onShowAllClick: () -> Unit,
    onFilmClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onShowAllClick) {
                Text(text = "Все")
            }
        }

        LazyRow(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            item { Spacer(modifier = Modifier.padding(start = 16.dp)) }

            items(films) { film ->
                FilmItem(
                    imageUrl = film.film.poster,
                    title = film.film.name,
                    rating = film.film.rating ?: "",
                    onItemClick = {
                        onFilmClick(film.film.filmId)
                        Log.d("film", "CategorySection:  ${film.film.filmId}")
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            item {
                ShowAllItem(
                    onClick = onShowAllClick
                )
            }
        }
    }
} 