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
import net.kotlincook.voting.actuator.VotingMetrics
import net.kotlincook.voting.model.Attitude.*
import net.kotlincook.voting.model.ModelSingleton
import net.kotlincook.voting.model.options
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

@Route("")
@StyleSheet("frontend://voting.css")
class Voting : VerticalLayout() {

    val logger = LoggerFactory.getLogger(Voting::class.java);
    val code: String?
    val answer = Label()

    @Autowired
    lateinit var votingMetrics: VotingMetrics

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

        options.forEachIndexed { i, option ->
            add(H3(option.descripion))
            // Kommentare
            add(CommentList(option.arguments))
            add(radioGroups[i])
        }
        add(voteButton)
        add(answer)

        code = VaadinService.getCurrentRequest().getParameter("code")

        voteButton.addClickListener {
            // val ip = UI.getCurrent().session.browser.address
//            val valid = Authenticator.isCodeValid(code)
            val valid = VALID
            logger.info("Voting successful")
            votingMetrics.countVoting()

            answer.text =
                    when (valid) {
                        VALID -> "Vielen Dank für Deine Teilnahme!"
                        EXPIRED -> "Die Zeit für die Teilnahme ist bereits abgelaufen."
                        INVALID -> "Ungültiger Code! Bei Problemen wende Dich bitte an Jörg."
                        USED -> "Du hast bereits abgestimmt."
                    }
            if (valid == VALID) {
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
}