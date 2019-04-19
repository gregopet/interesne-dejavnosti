import com.fasterxml.jackson.databind.ObjectMapper
import org.pac4j.core.authorization.Authorizer
import org.pac4j.core.profile.UserProfile
import org.pac4j.http.client.direct.DirectBasicAuthClient
import org.pac4j.http.profile.HttpProfile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.error.ClientErrorHandler
import ratpack.error.ServerErrorHandler
import org.jooq.DSLContext
import org.pac4j.http.client.indirect.FormClient
import ratpack.groovy.handling.GroovyChain
import ratpack.pac4j.RatpackPac4j
import ratpack.server.BaseDir
import ratpack.server.internal.DefaultServerConfig
import ratpack.session.SessionModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import asset.pipeline.ratpack.AssetPipelineModule
import asset.pipeline.ratpack.AssetPipelineHandler
import si.francebevk.db.Config
import si.francebevk.db.HikariDataSourceFactory
import si.francebevk.db.HikariShutdownService
import si.francebevk.db.enums.ActivityLogType
import si.francebevk.interesnedejavnosti.ActivityLogDAO
import si.francebevk.interesnedejavnosti.Admin
import si.francebevk.interesnedejavnosti.AdminConfig
import si.francebevk.interesnedejavnosti.DbAuthenticator
import si.francebevk.interesnedejavnosti.Deadlines
import si.francebevk.interesnedejavnosti.EmailConfig
import si.francebevk.interesnedejavnosti.EmailDispatch
import si.francebevk.interesnedejavnosti.FileConfig
import si.francebevk.interesnedejavnosti.LoginPage
import si.francebevk.interesnedejavnosti.MainPage
import si.francebevk.ratpack.error.UncaughtErrorHandler
import si.francebevk.ratpack.RockerRenderer

import static ratpack.groovy.Groovy.ratpack

ratpack {
    Logger LOG = LoggerFactory.getLogger("Ratpack bootstrap script")

    bindings {
        try {
            add ObjectMapper, new ObjectMapper().registerModule(new KotlinModule())

            final config = DefaultServerConfig.of {
                it.configureObjectMapper { it.registerModule(new KotlinModule()) }
                if (System.getProperty("config.file")) {
                    it.sysProps().env().yaml(System.getProperty("config.file")).build()
                } else {
                    it.sysProps().env().yaml("${BaseDir.find()}/config.yaml").build()
                }
            }

            if (config.development) {
                LOG.warn("Development environment active")
            }

            // database & JOOQ
            bindInstance Config, config.get("/database", Config)
            bindInstance EmailConfig, config.get("/smtp", EmailConfig)
            bindInstance FileConfig, config.get("/files", FileConfig)
            bindInstance Deadlines, config.get("/deadlines", Deadlines)
            bindInstance AdminConfig, config.get("/admin", AdminConfig)
            module HikariDataSourceFactory
            add HikariShutdownService.INSTANCE

            //template rendering
            add RockerRenderer.INSTANCE

            // Error renderer
            bindInstance(ClientErrorHandler, UncaughtErrorHandler.INSTANCE)
            bindInstance(ServerErrorHandler, UncaughtErrorHandler.INSTANCE)

            // Sessions
            module SessionModule

            // Assets
            moduleConfig(new AssetPipelineModule(), new AssetPipelineModule.Config(sourcePath: "../../../src/assets"))
        } catch (Exception x) {
            LOG.error("Error bootstrapping application", x)
            throw x
        }
    }

    handlers { AdminConfig cfg ->
        def authenticator = new DbAuthenticator(registry.get(DSLContext))
        def parameterClient = new FormClient("/login-form", authenticator)

        get("favicon.ico") { ctx -> ctx.response.status(404).send() }
        get("jasmine-tests") { ctx -> ctx.render(Tests.template())}

        prefix("assets") { chain ->
            chain.all(AssetPipelineHandler)
            chain.all { it.response.status(404).send("Asset not found") }
        }

        // All admin requests "peel off" here, avoiding the usual handler tree
        prefix("admin", new Admin(cfg))

        // Database authentication (=parents) is no good after this point!
        all { ctx ->
            RatpackPac4j.userProfile(ctx).then {
                if (it.isPresent() && it.get().id == null) {
                    RatpackPac4j.logout(ctx).then { ctx.next() }
                } else {
                    ctx.next()
                }
            }
        }

        get("login-form", LoginPage.INSTANCE)

        // Authentication setup
        all(RatpackPac4j.authenticator("login", parameterClient)) //init auth framework

        path("logout") { c ->
            // try logging that the parent had aborted, while we still have the profile
            RatpackPac4j.userProfile(c).then {
                it.ifPresent { profile ->
                    ActivityLogDAO.INSTANCE.insertEvent(ActivityLogType.abort, profile.id.toLong(), false, null, c.get(DSLContext))
                }
            }

            // now for the actual logout..
            RatpackPac4j.logout(c).then { c.redirect("/") }}
        post("login") { c -> RatpackPac4j.login(c, FormClient).then {
            c.redirect("/")
        } }

        // REST endpoint to check if user is authorized
        get("auth-check") { ctx ->
            RatpackPac4j.userProfile(ctx).then { prof ->
                def status = prof.map { 200 }.orElse(400)
                ctx.response.status(status).send()
            }
        }

        // ... authentication required from this point on
        all(RatpackPac4j.requireAuth(FormClient))

        all { ctx ->
            // Pre-prepare a permissions object for downstream
            RatpackPac4j.userProfile(ctx).then { profile ->
                def profileObj = profile.get()
                ctx.request.add(profileObj)
                //ctx.request.add(new Permissions(profileObj.roles))
                ctx.next()
            }
        }

        insert(MainPage.INSTANCE)
    }
}
