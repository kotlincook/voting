package net.kotlincook.voting.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinService
import net.kotlincook.voting.Authentication.AuthResult.*
import net.kotlincook.voting.Authenticator
import net.kotlincook.voting.model.*
import net.kotlincook.voting.model.Attitude.*
import java.lang.IllegalStateException


@Route("voting")
@StyleSheet("frontend://voting.css")
class Voting() : VerticalLayout() {

    val oneOption = Option("Ich bin für Frühling")
    val voteModel = VoteModel(oneOption)
    val code: String?
    val proHeader = TextField()

    val binder = Binder(TwoOptionsModel.TextFieldBean::class.java).apply {
        forField(proHeader).bind("value")
    }

    val RADIO_YES  = "bin ich dabei";
    val RADIO_IRR = "ist mir gleich";
    val RADIO_NO = "lehne ich ab";
    val radioButtons = RadioButtonGroup<String>().apply {
        setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
    }

    init {
        code = VaadinService.getCurrentRequest().getParameter("code")
        setId("chess_board")
        className = "main"

        val button = Button("Abstimmen")
        val label = Label()
        add(radioButtons)
        add(button)
        add(label)
        add(proHeader)

        button.addClickListener {
            // val ip = UI.getCurrent().session.browser.address
            val valid = Authenticator.isCodeValid(code)

            label.text =
                when (valid) {
                    OK -> "Vielen Dank für Deine Teilnahme!"
                    EXPIRE -> "Die Zeit für die Teilnahme ist bereits abgelaufen."
                    INVALID -> "Fehler bei der Anmeldung; bitte wende Dich an Jörg."
                    USED -> "Du hattest bereits teilgenommen."
                }
            if (valid == OK)  {
                when (radioButtons.value) {
                    RADIO_YES -> voteModel.addAttitude(oneOption, YES)
                    RADIO_IRR -> voteModel.addAttitude(oneOption, IRR)
                    RADIO_NO  -> voteModel.addAttitude(oneOption, NO)
                    else -> throw IllegalStateException("No Radio Button selected")
                }
            }
            button.isEnabled = false
        }

        binder.readBean(textFieldBean)

    }
}