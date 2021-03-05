package net.kotlincook.voting.util

import java.text.SimpleDateFormat
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

val HOST = "mail.gmx.net"
val SMTP_USERNAME = "kotlincook@gmx.net"
val SMTP_PASSWORD = "J3o1e4r1g"

fun sendMail(emailaddess: String, code: Long) {

    val subject = "Abstimmung 'Entscheidungsfindung durch Konsent-Beschluss'"
    val body = """
        <h1>Willkommen zu Crefo Vote</h1>
        <p>Klicke auf Deinen pers&ouml;nlichen Link
        <a href='https://crefovote.herokuapp.com/voting?code=${code}'>Voting App</a>, um zur Abstimmung zu gelangen.
        Der Link enth&auml;lt einen Code, der bis zum 13.2.2019 g&uuml;ltig ist und nur <u>einmal</u>
        verwendet werden kann.</p>
        <p>Happy Voting!</p>
    """.trimIndent()

    val props = System.getProperties()
    props["mail.transport.protocol"] = "smtp"
    props["mail.smtp.port"] = 587
    props["mail.smtp.starttls.enable"] = "true"
    props["mail.smtp.auth"] = "true"

    val session = Session.getDefaultInstance(props)

    val msg = MimeMessage(session)
    msg.setFrom(InternetAddress("kotlincook@gmx.net", "Crefo Vote"))
    msg.addRecipient(Message.RecipientType.TO,InternetAddress(emailaddess))
    msg.subject = subject
    msg.setContent(body, "text/html")
    val transport = session.transport

    try {
        println("Sending...")
        transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD)
        transport.sendMessage(msg, msg.allRecipients)
        println("Email to ${emailaddess} sent!")
    }
    catch (ex: Exception) {
        println("The email to ${subject} was not sent. Error: " + ex.message)
    }
    finally {
        transport.close()
    }
}


fun main() {

    val recipients0 = listOf(
        "A.Mecky@verband.creditreform.de",
        "a.schaefer@verband.creditreform.de",
        "a.rotering@verband.creditreform.de",
        "a.rauch@verband.creditreform.de",
        "c.landsky@verband.creditreform.de",
        "c.iven@verband.creditreform.de",
        "c.turkalj@verband.creditreform.de",
        "d.thom@verband.creditreform.de",
        "e.tuzar@verband.creditreform.de",
        "e.wulfert@verband.creditreform.de",
        "f.vollmar@verband.creditreform.de",
        "h.salmann@verband.creditreform.de",
        "h.weber@verband.creditreform.de",
        "i.alesker@verband.creditreform.de",
        "j.devries@verband.creditreform.de",
        "j.bittner@verband.creditreform.de",
        "j.vollmer@verband.creditreform.de",
        "j.wagner@verband.creditreform.de",
        "k.sollmann@verband.creditreform.de",
        "m.besch@verband.creditreform.de",
        "m.bracht@verband.creditreform.de",
        "Melanie.Kalka@sunzinet.com",
        "m.arnold@verband.creditreform.de",
        "o.hauch@verband.creditreform.de",
        "p.nill@verband.creditreform.de",
        "p.sittart@verband.creditreform.de",
        "p.mueller@verband.creditreform.de",
        "r.wedding@verband.creditreform.de",
        "r.wignanek@verband.creditreform.de",
        "Sascha.Cremers@sunzinet.com",
        "Sebastian.Spill@sunzinet.com",
        "s.kircher@verband.creditreform.de",
        "u.cirkel@verband.creditreform.de",
        "v.lutz@verband.creditreform.de")

    val recipients1 = listOf("joerg.vollmer@gmx.de")

    val parse = SimpleDateFormat("yyyy-MM-dd").parse("2019-02-13")
    val randoms: MutableSet<Int> = mutableSetOf()

    do {
        randoms += (Math.random() * 1000.0).toInt()
    }
    while (randoms.size < recipients1.size)

    randoms.forEachIndexed { i, random ->
        val code = (parse.time/100000 + random) * 2937 + 1311
        sendMail(recipients1[i], code)
    }
}
