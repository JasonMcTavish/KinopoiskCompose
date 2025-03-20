package ru.test.kinopoiskcompose.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.test.kinopoiskcompose.db.model.FilmImage
import ru.test.kinopoiskcompose.domain.GetGalleryByIdUseCase
import ru.test.kinopoiskcompose.ui.StateLoading
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getGalleryByIdUseCase: GetGalleryByIdUseCase
) : ViewModel() {

    private var imagesList: List<FilmImage> = emptyList()

    private val _images = MutableStateFlow<List<FilmImage>>(emptyList())
    val images = _images.asStateFlow()

    private val _loadGalleryState = MutableStateFlow<StateLoading>(StateLoading.Default)
    val loadGalleryState = _loadGalleryState.asStateFlow()

    private val _filters = MutableStateFlow<List<String>>(emptyList())
    val filters = _filters.asStateFlow()

    private val _imageFullScreen = MutableStateFlow<FilmImage?>(null)
    val imageFullScreen = _imageFullScreen.asStateFlow()


    fun setGallery(filmId: Int) {
        _loadGalleryState.value = StateLoading.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = getGalleryByIdUseCase.executeGalleryByFilmId(filmId)
                imagesList = response
                Log.d("response", "setGallery: $imagesList ")
                setFilters()
                _loadGalleryState.value = StateLoading.Success
            } catch (e: Throwable) {
                _loadGalleryState.value = StateLoading.Error(e.message.toString())
            }
        }
    }

    fun onSelectFilter(filter: String) {
        _images.value = imagesList.filter { it.imageCategory == filter }
    }

    private fun setFilters() {
        val tempList = mutableSetOf<String>()
        imagesList.forEach { image -> tempList.add(image.imageCategory) }
        _filters.value = tempList.toList()
        Log.d("filters", "setFilters: ${_filters.value} ")
    }

    fun setImageToFullScreen(image: FilmImage) {
        _imageFullScreen.update { image }
    }
}