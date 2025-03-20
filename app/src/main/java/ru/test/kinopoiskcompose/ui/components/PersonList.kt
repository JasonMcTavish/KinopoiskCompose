package ru.test.kinopoiskcompose.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PersonList(
    title: String,
    people: List<Person>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit,
    onShowAllClick: () -> Unit,
    showCount: Boolean = true
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp
            )
            if (showCount) {
                Text(
                    text = "${people.size}",
                    color = Color.Blue,
                    fontSize = 14.sp,
                    modifier = modifier.clickable { onShowAllClick() }
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(people.take(10)) { person ->
                PersonItem(
                    person = person,
                    onItemClick = { id ->
                        onItemClick(id)
                        Log.d("personList", "PersonList: $id")
                    }
                )
            }
        }
    }
}

@Composable
fun PersonItem(
    person: Person,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(80.dp)
            .clickable {
                onItemClick(person.id)
                Log.d("person", "PersonItem: ${person.id}")
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(person.photo)
                .crossfade(true)
                .build(),
            contentDescription = person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = person.name,
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )

        Text(
            text = person.role,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

data class Person(
    val id: Int,
    val name: String,
    val photo: String,
    val role: String
) 