package si.francebevk.interesnedejavnosti

import org.jooq.util.postgres.PostgresDSL
import org.slf4j.LoggerFactory
import ratpack.form.Form
import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import si.francebevk.db.Tables.PUPIL
import si.francebevk.db.Tables.PUPIL_GROUP
import si.francebevk.ratpack.async
import si.francebevk.ratpack.await
import si.francebevk.ratpack.jooq
import si.francebevk.ratpack.route

/**
 * Contains various administration functions.
 */
object Admin : Action<Chain> {

    private const val ADMIN_PASSWORD = "Aslkjnm234lk2j3mnsdf2342d34212nmfskldjfljgh4A"
    private val LOG = LoggerFactory.getLogger(Admin::class.java)

    override fun execute(t: Chain) = t.route {

        // simplified authentication
        all { ctx -> ctx.async {
                if (ctx.request.method.isPost) {
                    var form = ctx.parse(Form::class.java).await()
                    var password = form["password"]
                    if (password == ADMIN_PASSWORD) ctx.next()
                    else ctx.response.status(422).send()
                } else {
                    ctx.response.status(422).send()
                }
            }
        }

        post("welcome-emails", ::sendEmails)
    }


    fun sendEmails(ctx: Context) = ctx.async {
        val emailConfig = ctx.get(EmailConfig::class.java)
        val fileConfig = ctx.get(FileConfig::class.java)
        await {
            ctx.jooq.select(PUPIL.ID, PUPIL.EMAILS, PUPIL.ACCESS_CODE, PUPIL.NAME, PUPIL.PUPIL_GROUP, PUPIL_GROUP.YEAR)
            .from(PUPIL)
            .join(PUPIL_GROUP).on(PUPIL.PUPIL_GROUP.eq(PUPIL_GROUP.NAME))
            .orderBy(PUPIL.ID)
            .fetch { pupil ->
                if (pupil.getValue(PUPIL.EMAILS).isNotEmpty()) {
                    LOG.info("Sending welcome email to pupil ${pupil.getValue(PUPIL.ID)} ${pupil.getValue(PUPIL.NAME)} at emails ${pupil.getValue(PUPIL.EMAILS).joinToString()}")
                    EmailDispatch.sendWelcomeEmail(
                        to = pupil.getValue(PUPIL.EMAILS),
                        pupilName = pupil.getValue(PUPIL.NAME),
                        accessCode = pupil.getValue(PUPIL.ACCESS_CODE),
                        pupilClass = MainPage.translatePupilClass(pupil.getValue(PUPIL.PUPIL_GROUP), pupil.getValue(PUPIL_GROUP.YEAR)),
                        leaveTimesRelevant = MainPage.leaveTimesRelevant(pupil.getValue(PUPIL_GROUP.YEAR)),
                        config = emailConfig,
                        fileConfig = fileConfig
                    )
                } else {
                    LOG.warn("Not sending email for pupil ${pupil.getValue(PUPIL.ID)} because they have no email defined!")
                }
            }
        }
        LOG.info("All emails sent!")
        ctx.response.send("OK")
    }
}