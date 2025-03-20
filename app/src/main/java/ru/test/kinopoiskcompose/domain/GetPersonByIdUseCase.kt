package ru.test.kinopoiskcompose.domain

import ru.test.kinopoiskcompose.data.CinemaRepository
import ru.test.kinopoiskcompose.db.model.FilmsShortInfo
import ru.test.kinopoiskcompose.db.model.PersonFilms
import ru.test.kinopoiskcompose.db.model.PersonWithDetailInfo
import javax.inject.Inject

class GetPersonByIdUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    suspend fun executePersonDetailInfo(personId: Int): PersonWithDetailInfo {
        return repository.getPersonDetailInfo(personId)
    }

    suspend fun executeFilmShortInfo(filmId: Int): FilmsShortInfo {
        return repository.getFilmShortInfo(filmId)
    }

    suspend fun executeFilmsByPerson(personId: Int): List<PersonFilms> {
        return repository.getFilmsByPerson(personId)
    }
}