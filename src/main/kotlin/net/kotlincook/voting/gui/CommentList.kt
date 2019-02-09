package net.kotlincook.voting.gui

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

class CommentList(val labelList: MutableList<String> = ArrayList()) : VerticalLayout() {

    val plusImage = Image("frontend/add_button.png", "ADD").apply {
        className = "comment-plus-button"
        addListener(ClickEvent::class.java) {
            this@CommentList.add(newCommentField.apply {
                maxLength = 200
                value = ""
                focus()
            })
        }
    }

    val newCommentField = TextField().apply {
        className = "comment-new-comment"
        addKeyPressListener { e ->
            if (e.key.matches("Enter")) {
                add(newLabel(value))
                remove(this)
                labelList.add(value)
            }
        }
    }

    init {
        isPadding = false
        isSpacing = false
        add(HorizontalLayout().apply {
            add(H4("Eure Meinungen"), plusImage)
        })
        labelList.forEach {
            add(newLabel(it))
        }
    }

    fun newLabel(text: String) = Label("− $text").apply {
        className = "comment-label"
    }

}