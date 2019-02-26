package net.kotlincook.voting.gui

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

class CommentList (val labelList: MutableList<String> = ArrayList()) : VerticalLayout() {

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
        addKeyDownListener { e ->
            when {
                e.key.matches("Enter") -> {
                    remove(this)
                    if (value.isNotBlank()) {
                        add(newLabel(value))
                        labelList.add(value)
                    }
                }
                e.key.matches("Escape") -> {
                    remove(this)
                }
            }
        }
    }

    init {
        isPadding = false
        isSpacing = false
        add(HorizontalLayout().apply {
            add(H4("Einwände").apply {
                className = "doubts"
            })
            add(plusImage.apply {
                className = "comment-plus-button"
            })
        })
        labelList.forEach {
            add(newLabel(it))
        }
    }

    fun newLabel(text: String) = Label(text).apply {
        className = "comment-label"
    }

}