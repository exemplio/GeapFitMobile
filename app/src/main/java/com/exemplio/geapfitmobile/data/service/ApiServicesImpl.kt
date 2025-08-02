package com.exemplio.geapfitmobile.data.service

import ErrorResponse
import HttpUtil
import android.util.Log
import com.exemplio.geapfitmobile.domain.entity.CredentialModel
import com.exemplio.geapfitmobile.domain.entity.PasswordGrantRequest
import com.exemplio.geapfitmobile.domain.entity.Resultado
import com.exemplio.geapfitmobile.utils.MyUtils
import com.geapfit.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ApiServicesImpl(
    private val httpService: HttpServiceImpl,
    private val isOnlineProvider: IsOnlineProvider
) {
//    private val cache = Cache()

    private fun url(unencodedPath: String, queryParameters: Map<String, Any>? = null): Result<HttpUrl> {
        val isOnline = isOnlineProvider.isOnline()
        return try {
            if (isOnline) {
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

    private fun urlAuth(unencodedPath: String, queryParameters: Map<String, Any>? = null): Result<HttpUrl> {
        val isOnline = isOnlineProvider.isOnline()
        return try {
            if (isOnline) {
                val builder = HttpUrl.Builder()
                    .scheme("https")
                    .host(MyUtils.baseAuth)
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

    private suspend fun <T> httpCall(
        f: suspend (OkHttpClient) -> Response,
        parseJson: ((ResponseBody) -> T)? = null
    ): Resultado<T?> = withContext(Dispatchers.IO) {
        try {
            val response = httpService.response(f)
            val apiError = httpService.responseBody(f)?.let { Json.decodeFromString<ErrorResponse>(it) }
            HttpUtil.result(response, parseJson, apiError).let { result ->
                if (response.isSuccessful) {
                    Log.v("Success", "API call succeeded")
                    Resultado.success(response.body?.let { body ->
                        if (body.contentLength() > 0) {
                            val json = body.string()
                            Log.v("ResponseBody", "Response body: $json")
                            parseJson?.invoke(body)
                        } else {
                            Log.v("ResponseBody", "Empty response body")
                            null
                        }
                    })
                } else {
                    println("Annotation: ${result} - ${response.message}")
                    Resultado.identity(result)
                }
            }
        } catch (e: Exception) {
            Log.e("UnknownError", "Unexpected error", e)
            Resultado.failMsg("Ocurrió un error inesperado", error = 0)
        }
    }

    suspend fun passwordGrant(request: PasswordGrantRequest): Resultado<Any?> {
        val path = StaticNamesPath.passwordGrant.path
        val uri = urlAuth(
            "${MyUtils.typeAuth}$path",
            mapOf("key" to MyUtils.apiKey)
        )
        val headers = mapOf("Content-Type" to "application/json")
        return httpCall(
            f = { client ->
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = Json.encodeToString(request).toRequestBody(mediaType)
                val httpUrl = uri.getOrNull()
                    ?: throw uri.exceptionOrNull() ?: Exception("Invalid URL")
                Log.v("Request", "HTTP request URL: $httpUrl")
                Log.v("Request", "HTTP body: $body")
                val req = Request.Builder()
                    .url(httpUrl)
                    .post(body)
                    .headers(headers.toHeaders())
                    .build()
                client.newCall(req).execute()
            },
            parseJson = { json -> return@httpCall CredentialModel.fromJson(json.toString()) }
        )
    }

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