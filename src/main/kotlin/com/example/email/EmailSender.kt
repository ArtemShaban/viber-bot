package com.example.email

import mu.KotlinLogging
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail

class EmailSender {
    private val senderEmail = System.getenv("SENDER_EMAIL")
    private val senderEmailPassword = System.getenv("SENDER_EMAIL_PASSWORD")
    private val logger = KotlinLogging.logger { }

    //todo use coroutines to not block thread
    fun sendEmail(toMail: String, title: String, text: String) {
        if (senderEmail != null && senderEmailPassword != null) {
            logger.info { "Sending email to $toMail. Title='$title'. Text='$text'" }

            val email = HtmlEmail()
            email.hostName = "smtp.googlemail.com"
            email.setSmtpPort(465)
            email.setAuthenticator(DefaultAuthenticator(senderEmail, senderEmailPassword))
            email.isSSLOnConnect = true
            email.setFrom(senderEmail)
            email.addTo(toMail)
            email.subject = title
            email.setTextMsg(text)
            email.send()
        } else {
            logger.info { "Email credentials are empty. Skipping sending email to $toMail. Title='$title'. Text='$text'" }
        }
    }
}