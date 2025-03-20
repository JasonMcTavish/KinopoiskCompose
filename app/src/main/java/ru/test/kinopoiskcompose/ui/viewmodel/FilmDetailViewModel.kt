package ru.test.kinopoiskcompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.test.kinopoiskcompose.db.model.FilmMarkers
import ru.test.kinopoiskcompose.db.model.FilmWithDetailInfo
import ru.test.kinopoiskcompose.domain.GetFilmByIdUseCase
import ru.test.kinopoiskcompose.domain.GetFilmListUseCase
import ru.test.kinopoiskcompose.domain.GetFilmMarkersUseCase
import ru.test.kinopoiskcompose.domain.GetFilmsHistoryUseCase
import ru.test.kinopoiskcompose.domain.GetGenresCountriesUseCase
import ru.test.kinopoiskcompose.domain.GetTopFilmsUseCase
import ru.test.kinopoiskcompose.entity.ParamsFilterGallery
import ru.test.kinopoiskcompose.ui.StateLoading
import javax.inject.Inject

@HiltViewModel
class FilmDetailViewModel @Inject constructor(
    private val getGenresCountriesUseCase: GetGenresCountriesUseCase,
    private val getFilmListUseCase: GetFilmListUseCase,
    private val getTopFilmsUseCase: GetTopFilmsUseCase,
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val getFilmsHistoryUseCase: GetFilmsHistoryUseCase,
    private val getFilmMarkersUseCase: GetFilmMarkersUseCase
) : ViewModel() {
    private var currentFilmId = 0

    private val _filmDetailInfo = MutableStateFlow<FilmWithDetailInfo?>(null)
    val filmDetailInfo = _filmDetailInfo.asStateFlow()

    private val _loadCurrentFilmState = MutableStateFlow<StateLoading>(StateLoading.Default)
    val loadCurrentFilmState = _loadCurrentFilmState.asStateFlow()

    fun clearFilmData() {
        _filmDetailInfo.value = null
        _loadCurrentFilmState.value = StateLoading.Loading
        currentFilmId = 0
    }

    fun getFilmById(filmId: Int) {
        if (currentFilmId == filmId && _filmDetailInfo.value != null) {
            return
        }

        currentFilmId = filmId
        _filmDetailInfo.value = null
        _loadCurrentFilmState.value = StateLoading.Loading

        updateParamsFilterGallery()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getFilmsHistoryUseCase.addFilmToHistory(filmId)
                getFilmMarkersUseCase.addMarkers(filmId)
                val tempFilm: FilmWithDetailInfo =
                    getFilmByIdUseCase.executeFilmDetailInfoById(filmId)
                _filmDetailInfo.value = tempFilm
                _loadCurrentFilmState.value = StateLoading.Success
            } catch (e: Throwable) {
                _loadCurrentFilmState.value = StateLoading.Error(e.message.toString())
            }
        }
    }

    private fun updateParamsFilterGallery(
        filmId: Int = currentFilmId,
        galleryType: String = "STILL"
    ) {
        currentParamsFilterGallery =
            currentParamsFilterGallery.copy(filmId = filmId, galleryType = galleryType)
    }

    fun checkFilmInDB(filmId: Int): Flow<FilmMarkers?> {
        return getFilmMarkersUseCase.executeMarkersByFilm(filmId)
    }

    fun updateFilmMarkers(filmId: Int, isFavorite: Int, inCollection: Int, isViewed: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getFilmMarkersUseCase.updateMarkers(filmId, isFavorite, inCollection, isViewed)
        }
    }

    fun addNewCollection(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getFilmsHistoryUseCase.addNewCollection(name)
        }
    }

    fun addFIlmInCollection(filmId: Int, collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getFilmsHistoryUseCase.addNewFilmInCollections(filmId, collectionName)
        }
    }

    fun deleteFilmFromCollection(filmId: Int, collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getFilmsHistoryUseCase.deleteFilmFromCollections(filmId, collectionName)
        }
    }

    suspend fun checkFilmInCollection(collectionName: String, filmId: Int): Boolean {
        var temp = false
        viewModelScope.launch(Dispatchers.IO) {
            temp = getFilmsHistoryUseCase.checkFilmInCollection(collectionName, filmId) != 0
        }
        return temp
    }

    companion object {


        private var currentParamsFilterGallery = ParamsFilterGallery(
            filmId = 328,
            galleryType = "STILL"
        )
    }
}