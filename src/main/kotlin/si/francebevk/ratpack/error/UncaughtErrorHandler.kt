package si.francebevk.ratpack.error

import org.slf4j.LoggerFactory
import ratpack.error.internal.DefaultDevelopmentErrorHandler
import ratpack.error.internal.ErrorHandler
import ratpack.handling.Context

/**
 * Handles any uncaught errors in the handler chain.
 */
object UncaughtErrorHandler : ErrorHandler {

    private val LOG = LoggerFactory.getLogger("Error handling request")

    private val stacktraceErrorHandler = DefaultDevelopmentErrorHandler()

    /** Handle default client errors by status */
    override fun error(context: Context, statusCode: Int) {
        context.response.status(statusCode).send("An error has occured")
    }

    /** Handle server errors */
    override fun error(context: Context, throwable: Throwable) {
        if (context.request.contentType.isJson ) {
            LOG.error("${context.request.method} ${context.request.uri} (json)", throwable)
            JsonError.sendThrowable(context, 500, throwable)
        } else {
            val displayStacktrace = context.request.queryParams.containsKey("stacktrace")
            if (displayStacktrace) {
                stacktraceErrorHandler.error(context, throwable)
            } else {
                LOG.error("${context.request.method} ${context.request.uri}", throwable)
                context.response.status(500).send("An error has occured!")
            }
        }
    }
}
