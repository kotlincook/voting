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
import net.kotlincook.voting.model.TextFieldBean
import net.kotlincook.voting.model.textFieldBean


@Route("voting")
@StyleSheet("frontend://chess.css")
class Voting() : VerticalLayout() {
    val code: String?
    val proHeader = TextField()

    val binder = Binder(TextFieldBean::class.java).apply {
        forField(proHeader).bind("value")
    }


    init {
        code = VaadinService.getCurrentRequest().getParameter("code")
        setId("chess_board")
        className = "main"
        val radioButtons = RadioButtonGroup<String>()
        radioButtons.setItems("lehne ich ab", "ist mir gleich", "bin ich dabei")
        val button = Button("Abstimmen")
        val label = Label()
        add(radioButtons)
        add(button)
        add(label)
        add(proHeader)

        button.addClickListener {
            // val ip = UI.getCurrent().session.browser.address

            label.text =
                when (Authenticator.isCodeValid(code)) {
                    OK -> "Vielen Dank für Deine Teilnahme!"
                    EXPIRE -> "Die Zeit für die Teilnahme ist bereits abgelaufen."
                    INVALID -> "Fehler bei der Anmeldung; bitte wende Dich an Jörg."
                    USED -> "Du hattest bereits teilgenommen."
                }
            button.isEnabled = false
        }

        binder.readBean(textFieldBean)

    }
}