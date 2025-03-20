package ru.test.kinopoiskcompose.domain

import androidx.paging.PagingData
import ru.test.kinopoiskcompose.data.CinemaRepository
import ru.test.kinopoiskcompose.entity.ItemPerson
import ru.test.kinopoiskcompose.entity.ParamsFilterFilm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetPersonByNameUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    fun executePerson(personName: StateFlow<ParamsFilterFilm>): Flow<PagingData<ItemPerson>> {
        return repository.getPersonsByName(personName)
    }
}