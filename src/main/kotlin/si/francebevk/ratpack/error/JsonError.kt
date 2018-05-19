package si.francebevk.ratpack.error

import org.jooq.tools.json.JSONObject
import ratpack.handling.Context

/**
 * Represents a JSON error report that conforms to JSON API - http://jsonapi.org/
 */
object JsonError {
    /** JSON API's defined content type */
    const val JSON_API_CONTENT_TYPE = "application/vnd.api+json"

    /** An error that can be used to notify this handler the client was already notified (and the handler should take no action) */
    val ERROR_RESPONSE_SENT = Throwable("Error response was already sent")

    fun sendThrowable(ctx: Context, status: Int, throwable: Throwable, title: String? = null, logger: ((String, Throwable) -> Unit)? = null) {
        logger?.invoke("${ctx.request.method} ${ctx.request.uri} ${if (title != null) ": " + title else ""} (json)", throwable)
        if (throwable != ERROR_RESPONSE_SENT) {
            with(ctx.response) {
                status(status)
                contentType(JSON_API_CONTENT_TYPE)
                send(fromThrowable(status, throwable, title))
            }
        }
    }

    fun fromThrowable(status: Int, throwable: Throwable, title: String? = null) =
"""{
    "errors": [
        {
            "status" : $status,
            "title"  : "${JSONObject.escape(title ?: throwable.message ?: "")}",
            "detail" : "${JSONObject.escape(throwable.message)}"
        }
    ]
}"""

    fun fromText(status: Int, title: String, detail: String? = null) =
        if (detail != null)
"""{
    "errors": [
        {
            "status" : $status,
            "title"  : "${JSONObject.escape(title)}",
            "detail" : "${JSONObject.escape(detail)}"
        }
    ]
}""" else
"""{
    "errors": [
        {
            "status" : $status,
            "title"  : "${JSONObject.escape(title)}"
        }
    ]
}"""
}
