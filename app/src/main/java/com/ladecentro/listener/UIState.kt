package com.ladecentro.listener

sealed interface UIState<out T> {

    data class Success<T>(val data: T): UIState<T>

    data class Error(val errorResponse: String): UIState<Nothing>

    object Loading: UIState<Nothing>
}