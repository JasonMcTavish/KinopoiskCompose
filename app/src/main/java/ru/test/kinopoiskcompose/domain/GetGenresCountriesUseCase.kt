package ru.test.kinopoiskcompose.domain

import ru.test.kinopoiskcompose.data.RepositoryAPI
import ru.test.kinopoiskcompose.entity.ResponseGenresCountries
import javax.inject.Inject

class GetGenresCountriesUseCase @Inject constructor(private val repositoryAPI: RepositoryAPI) {
    suspend fun executeGenresCountries(): ResponseGenresCountries {
        return repositoryAPI.getGenresCountries()
    }
}