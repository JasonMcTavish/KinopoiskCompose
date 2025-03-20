package ru.test.kinopoiskcompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.test.kinopoiskcompose.app.prepareToShow
import ru.test.kinopoiskcompose.data.CategoriesFilms
import ru.test.kinopoiskcompose.data.TOP_TYPES
import ru.test.kinopoiskcompose.domain.GetFilmByIdUseCase
import ru.test.kinopoiskcompose.domain.GetFilmListUseCase
import ru.test.kinopoiskcompose.domain.GetFilmMarkersUseCase
import ru.test.kinopoiskcompose.domain.GetFilmsHistoryUseCase
import ru.test.kinopoiskcompose.domain.GetGenresCountriesUseCase
import ru.test.kinopoiskcompose.domain.GetTopFilmsUseCase
import ru.test.kinopoiskcompose.ui.StateLoading
import ru.test.kinopoiskcompose.ui.components.HomeList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGenresCountriesUseCase: GetGenresCountriesUseCase,
    private val getFilmListUseCase: GetFilmListUseCase,
    private val getTopFilmsUseCase: GetTopFilmsUseCase,
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val getFilmsHistoryUseCase: GetFilmsHistoryUseCase,
    private val getFilmMarkersUseCase: GetFilmMarkersUseCase,
) : ViewModel() {


    // HomePage
    private val _homePageList = MutableStateFlow<List<HomeList>>(emptyList())
    val homePageList = _homePageList.asStateFlow()

    private val _loadCategoryState = MutableStateFlow<StateLoading>(StateLoading.Default)
    val loadCategoryState = _loadCategoryState.asStateFlow()




    fun getFilmsByCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadCategoryState.value = StateLoading.Loading
                val list = listOf(
                    HomeList(
                        category = CategoriesFilms.BEST,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.BEST),
                            page = 1
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.PREMIERS,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = CategoriesFilms.PREMIERS.name,
                            page = null
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.AWAIT,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.AWAIT),
                            page = 1
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.POPULAR,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.POPULAR),
                            page = 1
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.TV_SERIES,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.TV_SERIES),
                            page = 1
                        ).prepareToShow(20)
                    )
                )
                _homePageList.value = list
                _loadCategoryState.value = StateLoading.Success
            } catch (e: Throwable) {
                _loadCategoryState.value = StateLoading.Error(e.message.toString())
            }
        }
    }


}