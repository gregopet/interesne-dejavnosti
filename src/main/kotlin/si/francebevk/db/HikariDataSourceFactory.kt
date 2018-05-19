package si.francebevk.db

import com.google.inject.Provides
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationVersion
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import ratpack.guice.ConfigurableModule
import ratpack.service.Service
import ratpack.service.StartEvent
import ratpack.service.StopEvent

private val LOG = LoggerFactory.getLogger(HikariDataSourceFactory::class.java)

/**
 * Constructs Hikari-pooled Postgres data source from connection parameters.
 */
class HikariDataSourceFactory : ConfigurableModule<Config>() {

    companion object {
        private val LOG = LoggerFactory.getLogger(HikariDataSourceFactory::class.java)
    }

    override fun configure() { }

    lateinit var simpleDataSource: DataSource
    val pooledDataSource by lazy {
        LOG.info("Spinning up Hikari")
        getPooledDataSource(simpleDataSource, 20)
    }


    @Provides
    @Singleton
    fun jooq(cfg: Config): DSLContext {
        simpleDataSource = getDataSource(cfg.host, cfg.port, cfg.database, cfg.username, cfg.password)
        return DSL.using(pooledDataSource, SQLDialect.POSTGRES)
    }

    @Provides
    @Singleton
    fun dataSource(cfg: Config): HikariDataSource {

        simpleDataSource = getDataSource(cfg.host, cfg.port, cfg.database, cfg.username, cfg.password)

        // Migrate the database to latest version
        if (System.getProperty("migrate.database", "false") == "true") {
            migrateDatabase(simpleDataSource)
        } else {
            LOG.warn("NOT migrating database, use -Dmigrate.database=true to run migrations!")
        }

        return pooledDataSource
    }

    private fun migrateDatabase(ds: DataSource) {
        LOG.warn("Migrating database")
        try {
            Flyway().also { fly ->
                fly.dataSource = ds
                fly.baselineVersion = MigrationVersion.fromVersion("1.23")
                fly.isCleanDisabled = true
                fly.baseline()
                fly.setLocations("classpath:/flyway")
            }.migrate()
        } catch (e: Throwable) {
            LOG.error("Error while migrating database", e)
            throw e
        }
        LOG.debug("Migration complete")
    }

    /** Create a datasource using all the parameters */
    private fun getDataSource(url: String, port: Int, database: String, username: String, password: String?) = PGSimpleDataSource().also {
        it.user = username
        it.password = password
        it.databaseName = database
        it.portNumber = port
        it.serverName = url
    }

    /** Create a pooled datasource */
    private fun getPooledDataSource(ds: DataSource, maxPoolSize: Int) : HikariDataSource {
        val cfg = HikariConfig().apply {
            leakDetectionThreshold = 200
            dataSource = ds
            maximumPoolSize = maxPoolSize
            poolName = "Main pool"
        }
        return HikariDataSource(cfg)
    }
}

/**
 * A service that can be registered to shut down the Hikari data pool.
 */
object HikariShutdownService : Service {

    override fun onStart(event: StartEvent) {
        // run migrations if they need to run, also force generating the datasource so we can catch any errors
        event.registry.get(HikariDataSource::class.java)
    }

    override fun onStop(event: StopEvent) {
        LOG.info("Shutting down Hikari")
        event.registry.get(HikariDataSource::class.java).close()
    }
}
