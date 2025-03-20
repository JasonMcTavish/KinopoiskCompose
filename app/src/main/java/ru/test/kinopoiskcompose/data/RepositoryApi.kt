package ru.test.kinopoiskcompose.data

import ru.test.kinopoiskcompose.api.CinemaApi
import ru.test.kinopoiskcompose.entity.ResponseGenresCountries
import javax.inject.Inject

class RepositoryAPI @Inject constructor(
    private val apiService: CinemaApi
) {
    suspend fun getGenresCountries(): ResponseGenresCountries = apiService.getGenresCountries()
}