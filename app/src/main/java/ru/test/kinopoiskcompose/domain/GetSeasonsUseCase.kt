package ru.test.kinopoiskcompose.domain

import ru.test.kinopoiskcompose.data.CinemaRepository
import javax.inject.Inject

class GetSeasonsUseCase @Inject constructor(private val repository: CinemaRepository) {
    fun executeSeasons(seriesId: Int) = repository.getSeasonsById(seriesId)
}