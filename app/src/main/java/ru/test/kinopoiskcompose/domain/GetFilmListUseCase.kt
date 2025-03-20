package ru.test.kinopoiskcompose.domain

import androidx.paging.PagingData
import ru.test.kinopoiskcompose.data.CinemaRepository
import ru.test.kinopoiskcompose.entity.FilmByFilter
import ru.test.kinopoiskcompose.entity.ParamsFilterFilm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetFilmListUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    fun executeFilmsByFilter(
        filters: StateFlow<ParamsFilterFilm>
    ): Flow<PagingData<FilmByFilter>> {
        return repository.getFilmsByFilter(filters)
    }
}