package ru.test.kinopoiskcompose.domain


import ru.test.kinopoiskcompose.data.CinemaRepository
import ru.test.kinopoiskcompose.db.model.FilmPersons
import javax.inject.Inject

class GetActorsListUseCase @Inject constructor(
    private val newRepository: CinemaRepository
) {
    suspend fun executePersonsList(filmId: Int): List<FilmPersons> {
        return newRepository.getPersonsByFilm(filmId)
    }
}