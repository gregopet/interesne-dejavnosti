package si.francebevk.interesnedejavnosti

import com.google.common.util.concurrent.RateLimiter
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.EmailConstants
import org.apache.commons.mail.HtmlEmail
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import si.francebevk.db.Tables.ERROR_LOG
import si.francebevk.dto.Activity
import si.francebevk.dto.PupilSettings
import javax.mail.internet.InternetAddress

object EmailDispatch {

    val LOG = LoggerFactory.getLogger(EmailDispatch::class.java)

    const val skipEmails = false
    const val skipCC = false
    const val SCHOOL_REPLY_ADDRESS = "prijave.osfblj@guest.arnes.si"
    const val SCHOOL_REPLY_NAME = "OŠ Franceta Bevka"

    private val rateLimit = RateLimiter.create(10.0)

    /**
     * Sends the invitation mail with the access code.
     *
     * @return true if sending was successful, false otherwise
     */
    fun sendWelcomeEmail(to: Array<String>,  pupilId: Long, jooq: DSLContext, pupilName: String, pupilClass: String, accessCode: String, leaveTimesRelevant: Boolean, config: EmailConfig, fileConfig: FileConfig): Boolean {
        return if (!skipEmails) {
            try {
                val message = config.startNewMessage()
                message.subject = if (leaveTimesRelevant) {
                    "Prijava v podaljšano bivanje in na interesne dejavnosti za učenca/učenko $pupilName"
                } else {
                    "Prijava na interesne dejavnosti za učenca/učenko $pupilName"
                }
                message.addTo(*to)
                message.setFrom(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)
                if (!skipCC) {
                    message.setCc(listOf(InternetAddress(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)))
                }
                message.setHtmlMsg(WelcomeMailHtml.template(pupilName, pupilClass, accessCode, MainPage.formattedStartDate, MainPage.formattedStartTime, MainPage.formattedEndDate, MainPage.formattedEndTime).render().toString())
                message.setTextMsg(WelcomeMailPlain.template(pupilName, pupilClass, accessCode, MainPage.formattedStartDate, MainPage.formattedStartTime, MainPage.formattedEndDate, MainPage.formattedEndTime).render().toString())
                message.attach(EmailAttachment().apply {
                    disposition = EmailAttachment.ATTACHMENT
                    description = "Katalog interesnih dejavnosti"
                    name = "Katalog-interesnih-dejavnosti_2018-2019.pdf"
                    path = fileConfig.cataloguePath
                })
                rateLimit.acquire()
                message.send()
                PupilDAO.updateEmailSent(pupilId, jooq)
                LOG.info("Email was sent!")
                true
            } catch (ex: Exception) {
                LOG.error("Error sending welcome email for pupil $pupilId to ${to.joinToString()}", ex)
                logError(pupilId, jooq, ex)
                false
            }
        } else {
            // fake success
            LOG.debug("Mails are not actually sent due to debug setting!")
            true
        }
    }

    /**
     * Sends the reopening mail with the access code.
     *
     * @return true if sending was successful, false otherwise
     */
    fun sendReopeningEmail(to: Array<String>, pupilId: Long, jooq: DSLContext, pupilName: String, pupilClass: String, accessCode: String, leaveTimesRelevant: Boolean, config: EmailConfig, fileConfig: FileConfig): Boolean {
        return if (!skipEmails) {
            try {
                val message = config.startNewMessage()
                message.subject = if (leaveTimesRelevant) {
                    "Podaljšanje prijav v podaljšano bivanje in na interesne dejavnosti za učenca/učenko $pupilName"
                } else {
                    "Podaljšanje prijav na interesne dejavnosti za učenca/učenko $pupilName"
                }
                message.addTo(*to)
                message.setFrom(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)
                if (!skipCC) {
                    message.setCc(listOf(InternetAddress(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)))
                }
                message.setTextMsg(ReopeningMailPlain.template(pupilName, pupilClass, accessCode).render().toString())
                rateLimit.acquire()
                message.send()
                PupilDAO.updateEmailSent(pupilId, jooq)
                LOG.info("Email was sent!")
                true
            } catch (ex: Exception) {
                LOG.error("Error sending reopneing email for pupil $pupilId to ${to.joinToString()}", ex)
                logError(pupilId, jooq, ex)
                false
            }
        } else {
            // fake success
            LOG.debug("Mails are not actually sent due to debug setting!")
            true
        }
    }

    /**
     * Sends the confirmation mail once people have finished editing the page.
     */
    fun sendConfirmationMail(to: Array<String>, pupilId: Long, jooq: DSLContext, pupilName: String, pupilClass: String, leaveTimes: PupilSettings, activities: List<Activity>, leaveTimesRelevant: Boolean, config: EmailConfig) {
        if (!skipEmails) {
            try {
                LOG.info("Sending confirmation email to ${to.joinToString()}")
                val message = config.startNewMessage()
                message.subject = if (leaveTimesRelevant) {
                    "Uspešna prijava v podaljšano bivanje in na interesne dejavnosti za učenca/učenko $pupilName"
                } else {
                    "Uspešna prijava na interesne dejavnosti za učenca/učenko $pupilName"
                }
                message.addTo(*to)
                message.setFrom(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)
                message.setCc(listOf(InternetAddress(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)))
                message.setTextMsg(
                    ConfirmationMailPlain.template(pupilName, pupilClass, leaveTimes, leaveTimesRelevant, activities).render().toString()
                )
                rateLimit.acquire()
                message.send()
            } catch (ex: Exception) {
                LOG.error("Error sending confirmation mail for pupil $pupilId to ${to.joinToString()}", ex)
                logError(pupilId, jooq, ex)
            }
        }
    }

    private fun logError(pupilId: Long, jooq: DSLContext, ex: Exception) = with(ERROR_LOG) {
        jooq.insertInto(ERROR_LOG)
        .set(PUPIL_ID, pupilId)
        .set(MESSAGE, ex.message)
        .execute()
    }


    private fun EmailConfig.startNewMessage() = HtmlEmail().also {
        it.hostName = host
        it.setSmtpPort(port)
        it.setAuthenticator(DefaultAuthenticator(username, password))
        it.isSSLOnConnect = ssl
        it.isStartTLSEnabled = startTls
        it.setFrom(from)
        it.setCharset(EmailConstants.UTF_8)
    }
}