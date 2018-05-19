package si.francebevk.ratpack
import org.jooq.DSLContext
import org.pac4j.core.profile.UserProfile
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.jackson.Jackson.json

/** Syntactic sugar for writing handling blocks in chains */
inline fun <T> T.route(block: T.() -> Unit) where T: Chain {
    block()
}

/** Get the jOOQ database context for this user */
val Context.jooq get() = get(DSLContext::class.java)

/** Get the currently logged in user */
val Context.user get() = get(UserProfile::class.java)

/**
 * Renders a [payload] object as json, possibly forcing the given http [status] and sends it, unless the payload is null (in which
 * case an empty response will be sent and the status will be changed to 204 unless overriden by the [status] parameter or set to
 * something other than 200 beforehand).
 */
internal fun <T> Context.renderJson(payload: T?, status: Int? = null, prettyPrint: Boolean = false) {
    if (status != null) response.status(status)
    if (payload == null) {
        if (status == null && this.response.status.code == 200) response.status(204)
        response.send()
    } else {
        if (prettyPrint) {
            render(json(payload, this.get(com.fasterxml.jackson.databind.ObjectMapper::class.java).writerWithDefaultPrettyPrinter()))
        } else {
            render(json(payload))
        }
    }
}
