package ru.test.kinopoiskcompose.entity

import android.graphics.drawable.Icon

data class BottomSheetItemDataModel(
    var includesCurrentFilm: Boolean,
    val collectionName: String,
    val filmsNumber: Int,
    val icon: Int
)

