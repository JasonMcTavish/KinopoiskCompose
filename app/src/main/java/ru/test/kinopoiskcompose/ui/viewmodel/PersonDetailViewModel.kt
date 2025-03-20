package ru.test.kinopoiskcompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.test.kinopoiskcompose.data.PROFESSIONS
import ru.test.kinopoiskcompose.db.model.FilmsShortInfo
import ru.test.kinopoiskcompose.db.model.PersonWithDetailInfo
import ru.test.kinopoiskcompose.domain.GetPersonByIdUseCase
import ru.test.kinopoiskcompose.ui.StateLoading
import javax.inject.Inject
import kotlin.collections.forEach
import kotlin.collections.sortedByDescending

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val getStaffByIdUseCase: GetPersonByIdUseCase
) : ViewModel() {
    private val _loadCurrentStaff = MutableStateFlow<StateLoading>(StateLoading.Loading)
    val loadCurrentStaff = _loadCurrentStaff.asStateFlow()

    private val _currentPerson = MutableStateFlow<PersonWithDetailInfo?>(null)
    val currentPerson = _currentPerson.asStateFlow()

    private val _films = MutableStateFlow<List<FilmsShortInfo>>(emptyList())
    val films = _films.asStateFlow()

    private val _filmsWithFilter = MutableStateFlow<List<FilmsShortInfo>>(emptyList())
    val filmsWithFilter = _filmsWithFilter.asStateFlow()

    private val _filters = MutableStateFlow<List<String>>(emptyList())
    val filters = _filters.asStateFlow()


    fun clearPersonData() {
        _currentPerson.value = null
        _loadCurrentStaff.value = StateLoading.Loading
        _films.value = emptyList()
    }

    fun getPersonDetail(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadCurrentStaff.value = StateLoading.Loading
            try {
                val temp = getStaffByIdUseCase.executePersonDetailInfo(personId)
                if (temp == null) {
                    _loadCurrentStaff.value =
                        StateLoading.Error("Не удалось загрузить информацию об актере")
                    return@launch
                }

                val films = temp.films
                val uniqueFilmIds = mutableSetOf<Int>()
                val tempFilmList = mutableListOf<FilmsShortInfo>()

                films?.forEach {
                    if (uniqueFilmIds.add(it.filmId)) { // Добавляем filmId только если его еще нет в сете
                        try {
                            val filmInfo = getStaffByIdUseCase.executeFilmShortInfo(it.filmId)
                            tempFilmList.add(filmInfo)
                        } catch (e: Exception) {
                            // Пропускаем фильм с ошибкой, но продолжаем загрузку остальных
                        }
                    }
                }

                // Сортируем по рейтингу (по убыванию)
                val sortedFilms = tempFilmList
                    .sortedByDescending { it.rating?.toFloatOrNull() ?: 0f }

                _films.value = sortedFilms
                _currentPerson.value = temp
                _loadCurrentStaff.value = StateLoading.Success
            } catch (e: Exception) {
                _loadCurrentStaff.value = StateLoading.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun setFilmsWithFilter(filter: String) {
        val filmsIdList: List<Int> = _currentPerson.value?.films
            ?.filter { it.professionKey == filter }
            ?.map { it.filmId } ?: emptyList()
        val tempFilmList = mutableSetOf<FilmsShortInfo>()
        filmsIdList.forEach { id ->
            tempFilmList.add(films.value.filter { it.filmId == id }.first())
        }
        _filmsWithFilter.value =
            tempFilmList.toMutableList().sortedByDescending { it.rating?.toFloatOrNull() ?: 0f }
    }

    fun setProfessionList() {
        val tempList = mutableSetOf<String>()
        _currentPerson.value?.films?.forEach { film ->
            if (PROFESSIONS.containsKey(film.professionKey)) film.professionKey?.let {
                tempList.add(it)
            }
        }
        _filters.value = tempList.toList()
    }
}