package ru.test.kinopoiskcompose.domain

import ru.test.kinopoiskcompose.data.CinemaRepository
import ru.test.kinopoiskcompose.db.model.FilmWithDetailInfo
import javax.inject.Inject

class GetFilmByIdUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    suspend fun executeFilmDetailInfoById(filmId: Int): FilmWithDetailInfo {
        return repository.getDetailInfoByFilm(filmId)
    }


}