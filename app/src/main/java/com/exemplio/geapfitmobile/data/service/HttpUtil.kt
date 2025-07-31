import com.geapfit.services.http.Result2
import okhttp3.Response

object HttpUtil {
    // Parse the response and wrap in Result2
    fun <T> result(
        response: Response,
        parseJson: ((String) -> T)? = null
    ): Result2<T> {
        return try {
            val body = response.body?.string().orEmpty()
            if (response.isSuccessful) {
                val obj = parseJson?.invoke(body)
                Result2(success = true, obj = obj, raw = body)
            } else {
                Result2(success = false, errorMessage = "HTTP ${response.code}: $body", raw = body)
            }
        } catch (e: Exception) {
            Result2(success = false, error = e, errorMessage = e.message, stackTrace = e.stackTraceToString())
        }
    }

    // Handle network or parsing failures
    fun <T> failResult(e: Throwable): Result2<T> =
        Result2(success = false, error = e, errorMessage = e.message, stackTrace = e.stackTraceToString())
}