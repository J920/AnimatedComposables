package com.j920.animatedcomposables.models


sealed class UiState<out T> {
    open var scrollIndex: Int = 0
    open var scrollOffset: Int = 0

    data object Initial : UiState<Nothing>()

    data object Loading : UiState<Nothing>()
    data class Success<T>(
        val data: T?,
        override var scrollIndex: Int,
        override var scrollOffset: Int
    ) : UiState<T>()

    data object Empty : UiState<Nothing>()
    data class Error(val message: String?) : UiState<Nothing>()
}