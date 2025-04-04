package ru.test.kinopoiskcompose.ui

sealed class StateLoading() {
    object Default : StateLoading()
    object Loading : StateLoading()
    object Success : StateLoading()
    class Error(private val message: String) : StateLoading()
}