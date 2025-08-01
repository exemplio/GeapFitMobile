package com.exemplio.geapfitmobile.data.service

import android.util.Log
import com.example.geapfit.models.CredentialModel
import com.exemplio.geapfitmobile.utils.MyUtils
import com.geapfit.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class ApiServicesImpl(
    private val httpService: HttpServiceImpl,
    private val isOnlineProvider: IsOnlineProvider
) {
//    private val cache = Cache()

    private fun url(unencodedPath: String, queryParameters: Map<String, Any>? = null): Result<HttpUrl> {
        val isOnline = isOnlineProvider.isOnline()
        return try {
            println("isOnline: $isOnline")
            if (isOnline) {
                println("isOnline2")
                val builder = HttpUrl.Builder()
                    .scheme("https")
                    .host(MyUtils.base)
                    .addEncodedPathSegments(unencodedPath)
                queryParameters?.forEach { (key, value) ->
                    builder.addQueryParameter(key, value.toString())
                }
                Result.success(builder.build())
            }
            else {
                println("isOnline3")
                Result.failure(IOException("No posee conexión a internet"))
            }
        } catch (e: Exception) {
            Log.e("UrlError", "Unexpected error in url builder", e)
            return Result.failure(e)
        }
    }

    private fun urlAuth(unencodedPath: String, queryParameters: Map<String, Any>? = null): HttpUrl {
        val builder = HttpUrl.Builder()
            .scheme("https")
            .host(MyUtils.baseAuth)
            .addEncodedPathSegments(unencodedPath)
        queryParameters?.forEach { (key, value) ->
            builder.addQueryParameter(key, value.toString())
        }
        return builder.build()
    }

    private suspend fun <T> httpCall(
        f: suspend (OkHttpClient) -> Response,
        parseJson: ((String) -> T)? = null
    ): Result<T?> = withContext(Dispatchers.IO) {
        try {
            val response = httpService.response(f)
            val result = HttpUtil.result(response, parseJson)
            Result.success(result.getOrNull())
        } catch (e: Exception) {
            Log.e("UnknownError", "Unexpected error", e)
            Result.failure(Exception("Ocurrió un error inesperado", e))
        }
    }

//    suspend fun passwordGrant(request: PasswordGrantRequest): Result2<CredentialModel?> {
//        val path = StaticNamesPath.passwordGrant.path
//        val uri = urlAuth(
//            "${MyUtils.typeAuth}$path",
//            mapOf("key" to MyUtils.apiKey)
//        )
//        val headers = mapOf("Content-Type" to "application/json")
//        return httpCall(
//            f = { client ->
//                val body = RequestBody.create(
//                    "application/json".toMediaTypeOrNull(),
//                    request.toJson()
//                )
//                val req = Request.Builder()
//                    .url(uri)
//                    .post(body)
//                    .headers(headers.toHeaders())
//                    .build()
//                client.newCall(req).execute()
//            },
//            parseJson = { json -> return@httpCall CredentialModel.fromJson(json) }
//        )
//    }
    suspend fun resendSign(param: CredentialModel): Result<Any> {
        return try {
            val params = mapOf("app-id" to MyUtils.clientId, "email" to param)
            val headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
                "app-id" to MyUtils.clientId
            )
            val uri = url(
                "${MyUtils.type}${MyUtils.type}${StaticNamesPath.recover.path}",
                params
            )
            val httpUrl = uri.getOrNull()
                ?: return Result.failure(uri.exceptionOrNull() ?: Exception("Invalid URL"))
            val response = httpCall(
                f = { client ->
                    val req = Request.Builder()
                        .url(httpUrl)
                        .get()
                        .headers(headers.toHeaders())
                        .build()
                    client.newCall(req).execute()
                },
                parseJson = { json -> json }
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recoveryQuestions(param: String): Result<Any> {
        val params = mapOf("app-id" to MyUtils.clientId, "email" to param)
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "app-id" to MyUtils.clientId
        )
        val uri = url(
            "${MyUtils.type}${MyUtils.type}${StaticNamesPath.recover.path}",
            params
        )
        val httpUrl = uri.getOrNull()
            ?: return Result.failure(uri.exceptionOrNull() ?: Exception("Invalid URL"))
        return try {
            val response = httpCall(
                f = { client ->
                    val req = Request.Builder()
                        .url(httpUrl)
                        .get()
                        .headers(headers.toHeaders())
                        .build()
                    client.newCall(req).execute()
                },
                parseJson = { json -> json }
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun getClients(): Result2<Users> {
//        val params = mapOf<String, String>()
//        val headers = mapOf("Content-Type" to "application/json")
//        val uri = url(
//            "${MyUtils.type}${StaticNamesPath.getClients.path}",
//            params
//        )
//        return httpCall(
//            f = { client ->
//                val req = Request.Builder()
//                    .url(uri)
//                    .get()
//                    .headers(headers.toHeaders())
//                    .build()
//                client.newCall(req).execute()
//            },
//            parseJson = { json -> return@httpCall Users.fromJson(json) }
//        )
//    }
}

// Extension functions and helpers
fun Map<String, String>.toHeaders(): Headers {
    val builder = Headers.Builder()
    this.forEach { (k, v) -> builder.add(k, v) }
    return builder.build()
}