package ru.test.kinopoiskcompose.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.test.kinopoiskcompose.api.CinemaApi
import ru.test.kinopoiskcompose.db.model.FilmWithGenres
import ru.test.kinopoiskcompose.model.FilmWithGenresModel
import javax.inject.Inject

class FilmsPager @Inject constructor(
    private val apiService: CinemaApi
) {
    fun getFilmsByCategory(categoryName: String): Flow<PagingData<FilmWithGenresModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { LoadTopPagingSource(apiService, categoryName) }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
} 