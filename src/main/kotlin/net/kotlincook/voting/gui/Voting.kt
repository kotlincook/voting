package net.kotlincook.voting.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinService
import net.kotlincook.voting.Authentication.AuthResult.*
import net.kotlincook.voting.Authenticator
import net.kotlincook.voting.model.Attitude.*
import net.kotlincook.voting.model.oneOption
import net.kotlincook.voting.model.voteModel


@Route("voting")
@StyleSheet("frontend://voting.css")
class   Voting : VerticalLayout() {

    val optionHeader = Label(oneOption.descripion)
    val answer = Label()
    val code: String?

//    val binder = Binder(TwoOptionsModel.TextFieldBean::class.java).apply {
//        forField(proHeader).bind("value")
//    }

    companion object {
        val RADIO_YES  = "ich bin dabei"
        val RADIO_IRR = "ist mir gleich"
        val RADIO_NO = "lehne ich ab"
    }
    val radioButtons = RadioButtonGroup<String>().apply {
        setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
    }

    init {
        className = "crefo"

        val voteButton = Button("Abstimmen").apply {
            isEnabled = false
        }
        add(H1("Crefo Vote"))
        add(optionHeader)
        add(radioButtons)
        add(voteButton)
        add(answer)

        code = VaadinService.getCurrentRequest().getParameter("code")
        radioButtons.addValueChangeListener {
            voteButton.isEnabled = true
        }

        voteButton.addClickListener {
            // val ip = UI.getCurrent().session.browser.address
            val valid = Authenticator.isCodeValid(code)

            answer.text =
                when (valid) {
                    OK -> "Vielen Dank für Deine Teilnahme!"
                    EXPIRED -> "Die Zeit für die Teilnahme ist bereits abgelaufen."
                    INVALID -> "Ungültiger Code! Bei Problemen wende Dich an Jörg..."
                    USED -> "Du hast bereits teilgenommen."
                }
            if (valid == OK)  {
                when (radioButtons.value) {
                    RADIO_YES -> voteModel.addAttitude(oneOption, YES)
                    RADIO_IRR -> voteModel.addAttitude(oneOption, IRR)
                    RADIO_NO  -> voteModel.addAttitude(oneOption, NO)
                    else -> throw IllegalStateException("No Radio Button selected")
                }
            }
            voteButton.isEnabled = false
        }

//        binder.readBean(textFieldBean)

    }
}