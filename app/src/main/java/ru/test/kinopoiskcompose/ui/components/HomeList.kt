package ru.test.kinopoiskcompose.ui.components

import ru.test.kinopoiskcompose.data.CategoriesFilms
import ru.test.kinopoiskcompose.db.model.FilmWithGenres

data class HomeList(
    val category: CategoriesFilms,
    val filmList: List<FilmWithGenres>
)