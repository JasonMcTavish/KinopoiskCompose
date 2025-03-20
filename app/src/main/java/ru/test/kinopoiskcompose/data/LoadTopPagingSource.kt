package ru.test.kinopoiskcompose.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.test.kinopoiskcompose.api.CinemaApi
import ru.test.kinopoiskcompose.app.converterInMonth
import ru.test.kinopoiskcompose.entity.convertToModelGenres
import ru.test.kinopoiskcompose.entity.convertToModelShortInfo
import ru.test.kinopoiskcompose.model.FilmWithGenresModel
import java.util.Calendar

class LoadTopPagingSource(
    private val apiService: CinemaApi,
    private val categoryName: String
) : PagingSource<Int, FilmWithGenresModel>() {

    override fun getRefreshKey(state: PagingState<Int, FilmWithGenresModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmWithGenresModel> {
        return try {
            val page = params.key ?: FIRST_PAGE

            val response = when (categoryName) {
                CategoriesFilms.PREMIERS.name -> {
                    if (page > FIRST_PAGE) {
                        return LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null
                        )
                    }
                    val year = Calendar.getInstance().get(Calendar.YEAR)
                    val month = (Calendar.getInstance().get(Calendar.MONTH) + 1).converterInMonth()
                    apiService.getPremier(year = year, month = month).films.map { film ->
                        FilmWithGenresModel(
                            film = film.convertToModelShortInfo(),
                            genres = film.convertToModelGenres()
                        )
                    }
                }

                CategoriesFilms.TV_SERIES.name -> {
                    apiService.getSerialsTop(type = categoryName, page = page).films.map { film ->
                        FilmWithGenresModel(
                            film = film.convertToModelShortInfo(),
                            genres = film.convertToModelGenres()
                        )
                    }
                }

                else -> {
                    apiService.getFilmsTop(type = categoryName, page = page).films.map { film ->
                        FilmWithGenresModel(
                            film = film.convertToModelShortInfo(),
                            genres = film.convertToModelGenres(),
                        )
                    }
                }
            }

            LoadResult.Page(
                data = response,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "load error: ${e.message}")
            LoadResult.Error(e)
        }
    }

    private companion object {
        private const val FIRST_PAGE = 1
        private const val TAG = "LoadTopPagingSource"
    }
} 