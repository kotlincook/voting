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
import net.kotlincook.voting.model.Attitude.*
import net.kotlincook.voting.model.ModelSingleton
import net.kotlincook.voting.model.Option
import net.kotlincook.voting.model.options


// https://crefovote.herokuapp.com/
@Route("voting")
@StyleSheet("frontend://voting.css")
class Voting : VerticalLayout() {
    val code: String?
    val answer = Label()

    companion object {
        val RADIO_YES = "ja, ich bin dafür"
        val RADIO_IRR = "ok, kann ich mit leben"
        val RADIO_NO = "nein, lehne ich ab!"
    }

    val voteButton = Button("Abstimmen").apply {
        className = "vote-button"
        isDisableOnClick = true
        isEnabled = false
    }

    val radioGroups = (1..options.size).map {
        RadioButtonGroup<String>().apply {
            setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
            addValueChangeListener {
                voteButton.isEnabled = canVoteButtonBeEnabled()
            }
        }
    }

    fun canVoteButtonBeEnabled(): Boolean {
        return radioGroups.all {
            it.value != null
        }
    }

    init {
        className = "voting"
        add(H1("Anonymous Voting App"))

        val optionBlocks = options.map {
            OptionBlock(it)
        }.forEach {
            add(it)
        }

        add(voteButton)
        add(answer)

        code = VaadinService.getCurrentRequest().getParameter("code")

        voteButton.addClickListener {
            // val ip = UI.getCurrent().session.browser.address
            val valid = Authenticator.isCodeValid(code)
//            val valid = OK

            answer.text =
                    when (valid) {
                        VALID -> "Vielen Dank für Deine Teilnahme!"
                        EXPIRED -> "Die Zeit für die Teilnahme ist bereits abgelaufen."
                        INVALID -> "Ungültiger Code! Bei Problemen wende Dich bitte an Jörg."
                        USED -> "Du hast bereits abgestimmt."
                    }
            if (valid == OK) {
                options.forEachIndexed { i, option ->
                    when (radioGroups[i].value) {
                        RADIO_YES -> ModelSingleton.addAttitude(option, YES)
                        RADIO_IRR -> ModelSingleton.addAttitude(option, OK)
                        RADIO_NO -> ModelSingleton.addAttitude(option, NO)
                        else -> throw IllegalStateException("No Radio Button selected")
                    }
                }
            }// if
        }//addClickListener
    }// init

//    private fun createOptionBlock(option: Option): Div {
//        return Div().apply {
//            add(H3(option.descripion))
//            // Kommentare
//            add(CommentList(option.arguments))
//            // Abstimm-Radio-Buttons:
//            add(RadioButtonGroup<String>().apply {
//                setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
//                addValueChangeListener {
//                    voteButton.isEnabled = canVoteButtonBeEnabled()
//                }
//            })
//        }
//    }


    class OptionBlock(option: Option): Div() {
        val radioGroup : RadioButtonGroup<String>
        init {
            add(H3(option.descripion))
            // Kommentare
            add(CommentList(option.arguments))
            radioGroup = RadioButtonGroup<String>().apply {
                setItems(RADIO_YES, RADIO_IRR, RADIO_NO)
            }.also { add(it) }
        }
    }

}