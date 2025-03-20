package ru.test.kinopoiskcompose.domain

import ru.test.kinopoiskcompose.data.CinemaRepository
import javax.inject.Inject

class GetGalleryByIdUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    suspend fun executeGalleryByFilmId(filmId: Int) =
        repository.getFilmGallery(filmId)
}