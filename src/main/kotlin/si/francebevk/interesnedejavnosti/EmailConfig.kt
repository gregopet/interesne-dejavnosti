package si.francebevk.interesnedejavnosti

/**
 * Represents a configuration of an SMTP server.
 * @property username The username to use when connecting to the server
 * @property password The password to use
 * @property from Who is this email from?
 * @property port Port of the SMTP server
 * @property host Host of the SMTP server
 * @property ssl Use SSL to connect?
 */
class EmailConfig(
    var username: String,
    var password: String,
    var from: String,
    var port: Int,
    var host: String,
    var ssl: Boolean = true
)