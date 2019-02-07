package net.kotlincook.voting.gui

import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route


@Route("result")
@StyleSheet("frontend://voting.css")
class Result() : VerticalLayout() {

    val resultLabel = Label("Hallo")

    init {
        className = "main"
        add(resultLabel)
    }
}