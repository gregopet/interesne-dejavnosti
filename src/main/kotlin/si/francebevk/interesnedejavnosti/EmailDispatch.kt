package si.francebevk.interesnedejavnosti

import com.google.common.util.concurrent.RateLimiter
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.EmailConstants
import org.apache.commons.mail.HtmlEmail
import org.slf4j.LoggerFactory
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
     * Example call: http -f POST http://localhost:5050/admin/welcome-emails password='Aslkjnm234lk2j3mnsdf2342d34212nmfskldjfljgh4A'
     */
    fun sendWelcomeEmail(to: Array<String>, pupilName: String, pupilClass: String, accessCode: String, leaveTimesRelevant: Boolean, config: EmailConfig, fileConfig: FileConfig) {
        if (!skipEmails) {
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
                message.setHtmlMsg(WelcomeMailHtml.template(pupilName, pupilClass, accessCode).render().toString())
                message.setTextMsg(WelcomeMailPlain.template(pupilName, pupilClass, accessCode).render().toString())
                message.attach(EmailAttachment().apply {
                    disposition = EmailAttachment.ATTACHMENT
                    description = "Katalog interesnih dejavnosti"
                    name = "Katalog-interesnih-dejavnosti_2018-2019.pdf"
                    path = fileConfig.cataloguePath
                })
                rateLimit.acquire()
                message.send()
                LOG.info("Email was sent!")
            } catch (ex: Exception) {
                LOG.error("Error sending welcome email to ${to.joinToString()}", ex)
            }
        }
    }

    /**
     * Sends the confirmation mail once people have finished editing the page.
     */
    fun sendConfirmationMail(to: Array<String>, pupilName: String, pupilClass: String, leaveTimes: PupilSettings, activities: List<Activity>, leaveTimesRelevant: Boolean, config: EmailConfig) {
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
                LOG.error("Error sending confirmation mail to ${to.joinToString()}", ex)
            }
        }
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