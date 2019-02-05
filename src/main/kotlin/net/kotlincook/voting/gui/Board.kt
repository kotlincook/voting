package net.kotlincook.voting.gui

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.router.Route
import net.kotlincook.voting.IpCollector


@Route("voting")
@StyleSheet("frontend://chess.css")
class Voting() : VerticalLayout() {

    init {
        setId("chess_board")
        className = "main"
        val radioButtons = RadioButtonGroup<String>()
        radioButtons.setItems("lehne ich ab", "ist mir gleich", "bin ich dabei")
        val button = Button("Abstimmen")
        val label = Label()
        add(radioButtons)
        add(button)
        add(label)

        button.addClickListener {
            val ip = UI.getCurrent().session.browser.address
            if (IpCollector.contains(ip)) {
                label.text = "Du hattest bereits abgestimmt :("
            }
            else {
                label.text = "Vielen Dank für Deine Teilnahme!"
                IpCollector.add(ip)
            }
            button.isEnabled = false
        }
    }
}