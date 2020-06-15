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
import si.francebevk.dto.AuthorizedPerson
import si.francebevk.dto.PupilSettings
import si.francebevk.viewmodel.MainPageForm
import javax.mail.internet.InternetAddress

object EmailDispatch {

    val LOG = LoggerFactory.getLogger(EmailDispatch::class.java)
    val EMAIL_CONTENT_LOG = LoggerFactory.getLogger("email.content")

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
    fun sendWelcomeEmail(to: Array<String>,  pupilId: Long, jooq: DSLContext, pupilName: String, pupilClass: String, accessCode: String, leaveTimesRelevant: Boolean, config: EmailConfig, fileConfig: FileConfig, deadlines: Deadlines, failedPupils: MutableList<String>): Boolean {
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
                val htmlMsg = WelcomeMailHtml.template(pupilName, pupilClass, accessCode, deadlines.startDateString, deadlines.startTimeString, deadlines.endDateString, deadlines.endTimeString).render().toString()
                val plainMsg = WelcomeMailPlain.template(pupilName, pupilClass, accessCode, deadlines.startDateString, deadlines.startTimeString, deadlines.endDateString, deadlines.endTimeString).render().toString()
                message.setHtmlMsg(htmlMsg)
                message.setTextMsg(plainMsg)
                if (fileConfig.cataloguePath.isNotBlank()) {
                    message.attach(EmailAttachment().apply {
                        disposition = EmailAttachment.ATTACHMENT
                        description = "Katalog interesnih dejavnosti"
                        name = "Katalog-interesnih-dejavnosti_2020-2021.pdf"
                        path = fileConfig.cataloguePath
                    })
                }
                rateLimit.acquire()
                message.send()
                PupilDAO.updateEmailSent(pupilId, jooq)
                LOG.info("Email was sent!")
                if (EMAIL_CONTENT_LOG.isTraceEnabled) {
                    EMAIL_CONTENT_LOG.trace(message.prettyPrint(plainMsg, htmlMsg))
                }
                true
            } catch (ex: Exception) {
                LOG.error("Error sending welcome email for pupil $pupilId to ${to.joinToString()}", ex)
                logError(pupilId, jooq, ex)
                failedPupils.add(pupilName)
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
    fun sendReopeningEmail(to: Array<String>, pupilId: Long, jooq: DSLContext, pupilName: String, pupilClass: String, accessCode: String, leaveTimesRelevant: Boolean, config: EmailConfig, fileConfig: FileConfig, failedPupils: MutableList<String>): Boolean {
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
                val plain = ReopeningMailPlain.template(pupilName, pupilClass, accessCode).render().toString()
                message.setTextMsg(plain)
                rateLimit.acquire()
                message.send()
                PupilDAO.updateEmailSent(pupilId, jooq)
                LOG.info("Email was sent!")
                if (EMAIL_CONTENT_LOG.isTraceEnabled) {
                    EMAIL_CONTENT_LOG.trace(message.prettyPrint(plain))
                }
                true
            } catch (ex: Exception) {
                LOG.error("Error sending reopneing email for pupil $pupilId to ${to.joinToString()}", ex)
                failedPupils.add(pupilName)
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
    fun sendConfirmationMail(to: Array<String>, pupilId: Long, jooq: DSLContext, vm: MainPageForm, payload: PupilSettings, activities: List<Activity>, config: EmailConfig) {
        if (!skipEmails && to.isNotEmpty()) {
            try {
                LOG.info("Sending confirmation email to ${to.joinToString()}")
                val message = config.startNewMessage()
                message.subject = if (vm.pupilHasExtendedStay) {
                    "Uspešna prijava v podaljšano bivanje in na interesne dejavnosti za učenca/učenko ${vm.pupilName}"
                } else {
                    "Uspešna prijava na interesne dejavnosti za učenca/učenko ${vm.pupilName}"
                }
                message.addTo(*to)
                message.setFrom(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)
                message.setCc(listOf(InternetAddress(SCHOOL_REPLY_ADDRESS, SCHOOL_REPLY_NAME)))
                val plain = composeEmail(vm, payload, activities)
                message.setTextMsg(plain)
                rateLimit.acquire()
                message.send()
                if (EMAIL_CONTENT_LOG.isTraceEnabled) {
                    EMAIL_CONTENT_LOG.trace(message.prettyPrint(plain))
                }
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
        it.setSslSmtpPort(port.toString())
        it.setAuthenticator(DefaultAuthenticator(username, password))
        it.isSSLOnConnect = ssl
        it.isStartTLSEnabled = startTls
        it.setFrom(from)
        it.setCharset(EmailConstants.UTF_8)
    }

    private fun HtmlEmail.prettyPrint(plain: String = "", html: String = ""): String =
"""
  To: ${toAddresses.map { it.address }.joinToString()}
  Subject: ${subject}

${plain.prependIndent("  ")}
"""
}