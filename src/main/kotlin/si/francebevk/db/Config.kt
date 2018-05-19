package si.francebevk.db

/**
 * The configuration required to access the database.
 * @property host The hostname of the database
 * @property port The port on which the database is listening
 * @property database Name of the database
 * @property username The username to use when connecting
 * @property password The password to use when connecting
 */
class Config(
    var host: String,
    var port: Int,
    var database: String,
    var username: String,
    var password: String?
)