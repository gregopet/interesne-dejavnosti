package si.francebevk.interesnedejavnosti

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.HtmlEmail
import org.slf4j.LoggerFactory
import si.francebevk.dto.Activity
import si.francebevk.dto.PupilSettings
import javax.mail.internet.InternetAddress

object EmailDispatch {

    val LOG = LoggerFactory.getLogger(EmailDispatch::class.java)

    const val skipEmails = false
    const val SCHOOL_REPLY_ADDRESS = "prijave.osfblj@guest.arnes.si"

    /**
     * Sends the invitation mail with the access code.
     */
    fun sendWelcomeEmail(to: String, pupilName: String, pupilClass: String, accessCode: String, config: EmailConfig, fileConfig: FileConfig) {
        if (!skipEmails) {
            LOG.info("Sending welcome email to $to")
            val message = config.startNewMessage()
            message.subject = "OŠ Franceta Bevka: prijava interesnih dejavnosti za učenca/učenko $pupilName"
            message.addTo(to)
            message.setFrom(SCHOOL_REPLY_ADDRESS)
            message.setCc(listOf(InternetAddress(SCHOOL_REPLY_ADDRESS)))
            message.setHtmlMsg(WelcomeMailHtml.template(pupilName, pupilClass, accessCode).render().toString())
            message.setTextMsg(WelcomeMailPlain.template(pupilName, pupilClass, accessCode).render().toString())
            message.attach(EmailAttachment().apply {
                disposition = EmailAttachment.ATTACHMENT
                description = "Katalog interesnih dejavnosti"
                name = "Katalog-interesnih-dejavnosti_2018-2019.pdf"
                path = fileConfig.cataloguePath
            })
            message.send()
        }
    }

    /**
     * Sends the confirmation mail once people have finished editing the page.
     */
    fun sendConfirmationMail(to: String, pupilName: String, pupilClass: String, leaveTimes: PupilSettings, activities: List<Activity>, leaveTimesRelevant: Boolean, config: EmailConfig) {
        if (!skipEmails) {
            LOG.info("Sending confirmation email to $to")
            val message = config.startNewMessage()
            message.subject = "OŠ Franceta Bevka: uspešna prijava interesnih dejavnosti za učenca/učenko $pupilName"
            message.addTo(to)
            message.setFrom(SCHOOL_REPLY_ADDRESS)
            message.setCc(listOf(InternetAddress(SCHOOL_REPLY_ADDRESS)))
            message.setTextMsg(
                    ConfirmationMailPlain.template(pupilName, pupilClass, leaveTimes, leaveTimesRelevant, activities).render().toString()
            )
            message.send()
        }
    }

    private fun EmailConfig.startNewMessage() = HtmlEmail().also {
        it.hostName = host
        it.setSmtpPort(port)
        it.setAuthenticator(DefaultAuthenticator(username, password))
        it.isSSLOnConnect = ssl
        it.setFrom(from)
        it.setCharset("UTF-8")
    }
}