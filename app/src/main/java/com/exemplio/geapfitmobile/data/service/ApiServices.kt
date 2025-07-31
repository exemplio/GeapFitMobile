package com.geapfit.services

import com.exemplio.geapfitmobile.data.service.HttpServiceImpl
import com.example.geapfit.models.CredentialModel
import com.example.geapfit.models.PasswordGrantRequest
import com.example.geapfit.models.Users
import com.exemplio.geapfitmobile.data.service.IsOnlineProvider
import com.geapfit.services.http.Result2
import com.geapfit.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class ApiServices(
    private val httpService: HttpServiceImpl,
    private val isOnlineProvider: IsOnlineProvider
) {
//    private val cache = Cache()

    private fun url(unencodedPath: String, queryParameters: Map<String, Any>? = null): HttpUrl {
        val builder = HttpUrl.Builder()
            .scheme("https")
            .host(MyUtils.base)
            .addEncodedPathSegments(unencodedPath)
        queryParameters?.forEach { (key, value) ->
            builder.addQueryParameter(key, value.toString())
        }
        return builder.build()
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

    suspend fun <T> httpCall(
        f: suspend (OkHttpClient) -> Response,
        parseJson: ((String) -> T)? = null
    ): Result2<T> = withContext(Dispatchers.IO) {
        val isOnline = isOnlineProvider.isOnline()
        if (isOnline) {
            try {
                val response = httpService.response(f)
                return@withContext HttpUtil.result(response, parseJson)
            } catch (e: Exception) {
                return@withContext HttpUtil.failResult(e)
            }
        }
        return@withContext Result2.failMsg("No posee conexión a internet")
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
    suspend fun resendSign(param: String): Result2<Any> {
        val params = mapOf("app-id" to MyUtils.clientId, "email" to param)
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "app-id" to MyUtils.clientId
        )
        val uri = url(
            "${MyUtils.type}${MyUtils.type}${StaticNamesPath.resend.path}",
            params
        )
        return httpCall(
            f = { client ->
                val req = Request.Builder()
                    .url(uri)
                    .get()
                    .headers(headers.toHeaders())
                    .build()
                client.newCall(req).execute()
            },
            parseJson = { json -> json }
        )
    }

    suspend fun recoveryQuestions(param: String): Result2<Any> {
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
        return httpCall(
            f = { client ->
                val req = Request.Builder()
                    .url(uri)
                    .get()
                    .headers(headers.toHeaders())
                    .build()
                client.newCall(req).execute()
            },
            parseJson = { json -> json }
        )
    }

    suspend fun closeSession(idToken: String): Result2<Void?> {
        val uri = url(
            "${MyUtils.type}${MyUtils.type}${StaticNamesPath.closeSession.path}"
        )
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $idToken"
        )
        return httpCall(
            f = { client ->
                val req = Request.Builder()
                    .url(uri)
                    .put(RequestBody.create(null, ByteArray(0)))
                    .headers(headers.toHeaders())
                    .build()
                client.newCall(req).execute()
            }
        )
    }

    suspend fun getClients(): Result2<Users> {
        val params = mapOf<String, String>()
        val headers = mapOf("Content-Type" to "application/json")
        val uri = url(
            "${MyUtils.type}${StaticNamesPath.getClients.path}",
            params
        )
        return httpCall(
            f = { client ->
                val req = Request.Builder()
                    .url(uri)
                    .get()
                    .headers(headers.toHeaders())
                    .build()
                client.newCall(req).execute()
            },
            parseJson = { json -> return@httpCall Users.fromJson(json) }
        )
    }
}

// Extension functions and helpers
fun Map<String, String>.toHeaders(): Headers {
    val builder = Headers.Builder()
    this.forEach { (k, v) -> builder.add(k, v) }
    return builder.build()
}