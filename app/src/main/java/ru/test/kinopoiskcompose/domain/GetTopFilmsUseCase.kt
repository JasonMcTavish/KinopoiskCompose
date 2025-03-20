package ru.test.kinopoiskcompose.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.test.kinopoiskcompose.data.CinemaRepository
import ru.test.kinopoiskcompose.db.model.FilmWithGenres
import ru.test.kinopoiskcompose.model.FilmWithGenresModel
import javax.inject.Inject

class GetTopFilmsUseCase @Inject constructor(
    private val repository: CinemaRepository
) {

    suspend fun executeTopFilms(
        topType: String,
        page: Int? = 1,
    ): List<FilmWithGenres> {
        return repository.getFilmsTopByCategoryList(topType, page)
    }

    fun executeTopFilmsPaging(categoryName: String): Flow<PagingData<FilmWithGenres>> {
        return repository.getFilmsTopByCategoryPaging(categoryName)
    }

    fun executeTopFilmsPagingModel(categoryName: String): Flow<PagingData<FilmWithGenresModel>> {
        return repository.getFilmsTopByCategoryPagingModel(categoryName)
    }
}