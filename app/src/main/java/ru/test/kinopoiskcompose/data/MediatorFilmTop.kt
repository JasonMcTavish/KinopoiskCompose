package ru.test.kinopoiskcompose.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ru.test.kinopoiskcompose.api.CinemaApi
import ru.test.kinopoiskcompose.app.converterInMonth
import ru.test.kinopoiskcompose.db.CinemaDao
import ru.test.kinopoiskcompose.db.model.FilmWithGenres
import ru.test.kinopoiskcompose.db.model.NewFilmTopType
import ru.test.kinopoiskcompose.entity.ParamsFilterFilm
import ru.test.kinopoiskcompose.entity.convertForDbGenres
import ru.test.kinopoiskcompose.entity.convertForDbShortInfo
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar

@OptIn(ExperimentalPagingApi::class)
class MediatorFilmTop(
    private val apiService: CinemaApi,
    private val apiDatabase: CinemaDao,
    private val categoryName: String,
    private val filters: ParamsFilterFilm = ParamsFilterFilm(),
    private val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    private val month: String = (Calendar.getInstance().get(Calendar.MONTH) + 1).converterInMonth()
) : RemoteMediator<Int, FilmWithGenres>() {

    private var pageIndex = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FilmWithGenres>
    ): MediatorResult {
        pageIndex = getIndex(loadType) ?: return MediatorResult.Success(true)

        return try {
            if (loadType == LoadType.REFRESH) {
                apiDatabase.clearFilmGenres()
                apiDatabase.clearFilmTopTypesByCategory(categoryName)
            }

            when (categoryName) {
                CategoriesFilms.PREMIERS.name -> {
                    val response = apiService.getPremier(year = year, month = month).films
                    if (response.isNotEmpty()) {
                        val filmsForDb = response.map { film ->
                            apiDatabase.insertFilmGenres(film.convertForDbGenres())
                            film.convertForDbShortInfo()
                        }
                        apiDatabase.insertFilmTopTypes(filmsForDb.map {
                            NewFilmTopType(filmId = it.filmId, categoryName = categoryName)
                        })
                        apiDatabase.insertFilmShortInfo(filmsForDb)
                    }
                    MediatorResult.Success(endOfPaginationReached = response.isEmpty())
                }

                CategoriesFilms.TV_SERIES.name -> {
                    val response = apiService.getSerialsTop(
                        type = categoryName,
                        page = pageIndex
                    ).films
                    val filmsForDb = response.map { film ->
                        apiDatabase.insertFilmGenres(film.convertForDbGenres())
                        film.convertForDbShortInfo()
                    }
                    apiDatabase.insertFilmTopTypes(filmsForDb.map {
                        NewFilmTopType(filmId = it.filmId, categoryName = categoryName)
                    })
                    apiDatabase.insertFilmShortInfo(filmsForDb)
                    MediatorResult.Success(endOfPaginationReached = response.isEmpty())
                }
                else -> {
                    val response =
                        apiService.getFilmsTop(type = categoryName, page = pageIndex).films
                    if (response.isNotEmpty()) {
                        val filmsForDb = response.map { film ->
                            apiDatabase.insertFilmGenres(film.convertForDbGenres())
                            film.convertForDbShortInfo()
                        }
                        apiDatabase.insertFilmTopTypes(filmsForDb.map {
                            NewFilmTopType(filmId = it.filmId, categoryName = categoryName)
                        })
                        apiDatabase.insertFilmShortInfo(filmsForDb)
                    }
                    MediatorResult.Success(endOfPaginationReached = response.isEmpty())
                }
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun getIndex(loadType: LoadType): Int? {
        return when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> null
            LoadType.APPEND -> ++pageIndex
        }
    }
}