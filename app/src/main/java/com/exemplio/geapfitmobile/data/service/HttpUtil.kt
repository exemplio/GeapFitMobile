import com.example.geapfit.models.Message
import com.exemplio.geapfitmobile.domain.entity.Resultado
import com.geapfit.services.http.Result2
import com.geapfit.utils.translate
import okhttp3.Response
import okhttp3.ResponseBody


object HttpUtil {

    fun <T> result(
        response: Response,
        func: ((ResponseBody) -> T)? = null,
        error: ErrorResponse? = null,
    ): Resultado<T?> {
        val jsonElement = response.body
        return when (response.code) {
            200 -> {
                if (func != null && jsonElement != null) {
                    val body = func(jsonElement)
                    Resultado.success(body)
                } else {
                    Resultado.success(null)
                }
            }

            202, 422 -> {
                val message = Message.fromJson(jsonElement.toString())
                val errorMsg = message?.message
                val cause = message?.cause

                var error = ""

                if (errorMsg != null) {
                    error += translate(errorMsg)
                }

                if (cause != null) {
                    val join = cause.map { translate(it) }.joinToString(", ")
                    if (join.isNotEmpty()) {
                        error += ", $join"
                    }
                }

                if (error.isEmpty()) {
                    error = "EMPTY_ERROR"
                }

                Resultado.msg<T?>(message, errorMessage = error)
            }

            400 -> {
                Resultado.failMsg<T?>(errorMessage = error?.error?.message.toString(), error = response.code)
            }
            401 -> {
                Resultado.failMsg<T?>("Error de autenticación", error = response.code)
            }
            403 -> {
                Resultado.failMsg<T?>("No hay permisos para acceder a este recurso", error = response.code)
            }
            502 -> {
                Resultado.failMsg<T?>("Gateway timeout", error = response.code)
            }
            503 -> {
                Resultado.failMsg<T?>("Service Unavailable",error = response.code,)
            }
            else -> Resultado.failMsg<T?>(errorMessage = "${response.code} ${response.body?.string()}", error = response.code)
        }
    }

    // Handle network or parsing failures
    fun <T> failResult(e: Throwable): Result2<T> =
        Result2(success = false, error = e, errorMessage = e.message, stackTrace = e.stackTraceToString())
}