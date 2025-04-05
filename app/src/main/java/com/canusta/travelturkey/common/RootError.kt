package com.canusta.travelturkey.common

sealed interface RootError {
    enum class Network : RootError{
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        SERVER_ERROR,
        NO_INTERNET_CONNECTION,
        UNKNOWN
    }

}