package com.geapfit.services.http

data class Result2<T>(
    val success: Boolean,
    val obj: T? = null,
    val error: Throwable? = null,
    val stackTrace: String? = null,
    val errorMessage: String? = null,
    val raw: Any? = null // Optional: original response object
) {
    companion object {
        fun <T> failMsg(msg: String): Result2<T> =
            Result2(success = false, errorMessage = msg)
    }
}