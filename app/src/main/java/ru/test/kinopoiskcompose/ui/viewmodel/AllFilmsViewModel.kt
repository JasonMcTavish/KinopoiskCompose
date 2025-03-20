package ru.test.kinopoiskcompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.test.kinopoiskcompose.data.CategoriesFilms
import ru.test.kinopoiskcompose.data.TOP_TYPES
import ru.test.kinopoiskcompose.domain.GetFilmByIdUseCase
import ru.test.kinopoiskcompose.domain.GetFilmListUseCase
import ru.test.kinopoiskcompose.domain.GetFilmMarkersUseCase
import ru.test.kinopoiskcompose.domain.GetFilmsHistoryUseCase
import ru.test.kinopoiskcompose.domain.GetGenresCountriesUseCase
import ru.test.kinopoiskcompose.domain.GetTopFilmsUseCase
import ru.test.kinopoiskcompose.model.FilmWithGenresModel
import ru.test.kinopoiskcompose.ui.StateLoading
import javax.inject.Inject

@HiltViewModel
class AllFilmsViewModel @Inject constructor(
    private val getGenresCountriesUseCase: GetGenresCountriesUseCase,
    private val getFilmListUseCase: GetFilmListUseCase,
    private val getTopFilmsUseCase: GetTopFilmsUseCase,
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val getFilmsHistoryUseCase: GetFilmsHistoryUseCase,
    private val getFilmMarkersUseCase: GetFilmMarkersUseCase,
) : ViewModel() {


    private val _loadCategoryState = MutableStateFlow<StateLoading>(StateLoading.Default)
    val loadCategoryState = _loadCategoryState.asStateFlow()

    private var _allFilmsByCategory: MutableStateFlow<PagingData<FilmWithGenresModel>> =
        MutableStateFlow(value = PagingData.empty())
    val allFilmsByCategory: StateFlow<PagingData<FilmWithGenresModel>> = _allFilmsByCategory



    fun setCurrentCategory(category: CategoriesFilms) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadCategoryState.value = StateLoading.Loading
                val categoryForRequest =
                    if (category.name == CategoriesFilms.PREMIERS.name) category.name
                    else TOP_TYPES.getValue(category)
                getTopFilmsUseCase.executeTopFilmsPagingModel(categoryName = categoryForRequest)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        _allFilmsByCategory.value = it
                    }
                _loadCategoryState.value = StateLoading.Success
            } catch (e: Throwable) {
                _loadCategoryState.value = StateLoading.Error(e.message.toString())
            }
        }
    }

}