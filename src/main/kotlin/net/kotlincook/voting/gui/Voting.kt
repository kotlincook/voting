package net.kotlincook.voting.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinService
import net.kotlincook.voting.Authentication.AuthResult.*
import net.kotlincook.voting.Authenticator
import net.kotlincook.voting.model.Attitude.*
import net.kotlincook.voting.model.ModelSingleton
import net.kotlincook.voting.model.options

// https://crefovote.herokuapp.com/
@Route("voting")
@StyleSheet("frontend://voting.css")
class Voting : VerticalLayout() {
    val code: String?
    val answer = Label()

    val optionHeader0 = H3(options[0].descripion)
    val optionHeader1 = H3(options[1].descripion)

    companion object {
        val RADIO_YES = "ja, ich bin dafür"
        val RADIO_IRR = "ok, kann ich mit leben"
        val RADIO_NO  = "nein, lehne ich ab!"
    }

    val radioButtons0 = RadioButtonGroup<String>().apply {
        setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
        addValueChangeListener {
            voteButton.isEnabled = canVoteButtonBeEnabled()
        }
    }
    val radioButtons1 = RadioButtonGroup<String>().apply {
        setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
        addValueChangeListener {
            voteButton.isEnabled = canVoteButtonBeEnabled()
        }
    }

    fun canVoteButtonBeEnabled(): Boolean {
        return radioButtons0.value != null && radioButtons1.value != null
    }

    val voteButton = Button("Abstimmen").apply {
        className = "vote-button"
        isDisableOnClick = true
        isEnabled = false
    }

    init {
        className = "voting"

        add(H1("Anonymous Voting App"))
        add(optionHeader0)

        // Kommentare
        add(CommentList(options[0].arguments))
        add(radioButtons0)
        add(voteButton)

        add(optionHeader1)

        // Kommentare
        add(CommentList(options[1].arguments))
        add(radioButtons1)
        add(voteButton)

        add(answer)

        code = VaadinService.getCurrentRequest().getParameter("code")

        voteButton.addClickListener {
            // val ip = UI.getCurrent().session.browser.address
            val valid = Authenticator.isCodeValid(code)

            answer.text =
                    when (valid) {
                        OK -> "Vielen Dank für Deine Teilnahme!"
                        EXPIRED -> "Die Zeit für die Teilnahme ist bereits abgelaufen."
                        INVALID -> "Ungültiger Code! Bei Problemen wende Dich bitte an Jörg."
                        USED -> "Du hast bereits abgestimmt."
                    }
            if (valid == OK) {
                when (radioButtons0.value) {
                    RADIO_YES -> ModelSingleton.addAttitude(options[0], YES)
                    RADIO_IRR -> ModelSingleton.addAttitude(options[0], IRR)
                    RADIO_NO -> ModelSingleton.addAttitude(options[0], NO)
                    else -> throw IllegalStateException("No Radio Button selected")
                }
                when (radioButtons1.value) {
                    RADIO_YES -> ModelSingleton.addAttitude(options[1], YES)
                    RADIO_IRR -> ModelSingleton.addAttitude(options[1], IRR)
                    RADIO_NO -> ModelSingleton.addAttitude(options[1], NO)
                    else -> throw IllegalStateException("No Radio Button selected")
                }
            }
        }
    }
}