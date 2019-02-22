package net.kotlincook.voting.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinService
import net.kotlincook.voting.Authentication.AuthResult.*
import net.kotlincook.voting.Authenticator
import net.kotlincook.voting.gui.OptionBlock.Companion.RADIO_IRR
import net.kotlincook.voting.gui.OptionBlock.Companion.RADIO_NO
import net.kotlincook.voting.gui.OptionBlock.Companion.RADIO_YES
import net.kotlincook.voting.model.Attitude.*
import net.kotlincook.voting.model.ModelSingleton
import net.kotlincook.voting.model.Option
import net.kotlincook.voting.model.options

// https://crefovote.herokuapp.com/
@Route("voting")
@StyleSheet("frontend://voting.css")
class Voting : VerticalLayout() {

    val answer = Label()

    val voteButton = Button()

    val optionBlocks: List<OptionBlock>

    private fun canVoteButtonBeEnabled(): Boolean {
        return optionBlocks.all {
            it.radioGroup.value != null
        }
    }

    init {
        val code = VaadinService.getCurrentRequest().getParameter("code")

        className = "voting"
        add(H1("Anonymous Voting App"))

        optionBlocks = options.map {
            OptionBlock(it) {
                voteButton.isEnabled = canVoteButtonBeEnabled()
            }.also { add(it) }
        }

        add(voteButton.apply {
            className = "vote-button"
            text = "Abstimmen"
            isDisableOnClick = true
            isEnabled = false
        })
        add(answer)

        voteButton.addClickListener {
            // val ip = UI.getCurrent().session.browser.address
            val valid = Authenticator.isCodeValid(code)

            answer.text =
                    when (valid) {
                        VALID -> "Vielen Dank für Deine Teilnahme!"
                        EXPIRED -> "Die Zeit für die Teilnahme ist bereits abgelaufen."
                        INVALID -> "Ungültiger Code! Bei Problemen wende Dich bitte an Jörg."
                        USED -> "Du hast bereits abgestimmt."
                    }
            if (valid == VALID) {
                optionBlocks.forEach {
                    when (it.radioGroup.value) {
                        RADIO_YES -> ModelSingleton.addAttitude(it.option, YES)
                        RADIO_IRR -> ModelSingleton.addAttitude(it.option, OK)
                        RADIO_NO -> ModelSingleton.addAttitude(it.option, NO)
                        else -> throw IllegalStateException("No Radio Button selected")
                    }
                }
            }
        }// addClickListener
    }// init
}


class OptionBlock(val option: Option, radioButtonChangeListener: () -> Unit) : Div() {

    companion object {
        val RADIO_YES = "ja, ich bin dafür"
        val RADIO_IRR = "ok, kann ich mit leben"
        val RADIO_NO = "nein, lehne ich ab!"
    }

    val radioGroup: RadioButtonGroup<String>

    init {
        add(H3(option.descripion))
        // Kommentare
        add(CommentList(option.arguments))
        radioGroup = RadioButtonGroup<String>().apply {
            setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
        }.also {
            it.addValueChangeListener {
                radioButtonChangeListener()
            }
            add(it)
        }
    }
}// class