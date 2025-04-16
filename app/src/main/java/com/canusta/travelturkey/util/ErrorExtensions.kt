package com.canusta.travelturkey.util

import com.canusta.travelturkey.common.RootError

fun RootError.toLocalizedMessage(): String {
    return when (this) {
        is RootError.Network -> when (this) {
            RootError.Network.REQUEST_TIMEOUT -> "İstek zaman aşımına uğradı. Lütfen tekrar deneyin."
            RootError.Network.TOO_MANY_REQUESTS -> "Çok fazla istek gönderildi. Lütfen biraz bekleyip tekrar deneyin."
            RootError.Network.SERVER_ERROR -> "Sunucu hatası oluştu. Lütfen daha sonra tekrar deneyin."
            RootError.Network.NO_INTERNET_CONNECTION -> "İnternet bağlantısı yok. Lütfen bağlantınızı kontrol edin."
            RootError.Network.UNKNOWN -> "Bilinmeyen bir hata oluştu. Lütfen tekrar deneyin."
        }
    }
}