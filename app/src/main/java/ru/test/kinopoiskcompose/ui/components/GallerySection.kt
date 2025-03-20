package ru.test.kinopoiskcompose.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.test.kinopoiskcompose.db.model.FilmImage

@Composable
fun GallerySection(
    title: String,
    images: List<FilmImage>,
    modifier: Modifier = Modifier,
    onClickAll: () -> Unit
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
                text = title, color = Color.White, fontSize = 20.sp
            )
            Text(
                text = "Все",
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = modifier.clickable { onClickAll() }
            )

        }

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(top = 16.dp)
                .height(200.dp)
        ) {
            items(images.take(10)) { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(imageUrl.image)
                        .crossfade(true).build(),
                    contentDescription = imageUrl.imageCategory,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
} 