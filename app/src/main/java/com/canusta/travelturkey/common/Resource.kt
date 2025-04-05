package com.canusta.travelturkey.common

sealed interface Resource<out T, out E : RootError> {
    data class Success<out T, out E: RootError>(val data : T): Resource<T, E>
    data class Error<out T, out E: RootError>(val error: E): Resource<T, E>
}