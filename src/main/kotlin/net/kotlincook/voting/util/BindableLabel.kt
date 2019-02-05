package de.kotlincook.vaadin.vaadinutil

import com.vaadin.flow.component.HasValue.ValueChangeListener
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.shared.Registration


class BindableLabel : Label, HasValue<HasValue.ValueChangeEvent<String>, String> {

    constructor()

    constructor(text: String) : super(text)

    override fun setValue(value: String) {
        text = value
    }

    override fun getValue(): String {
        return text
    }

    override fun addValueChangeListener(p0: ValueChangeListener<in HasValue.ValueChangeEvent<String>>?): Registration {
        return Registration {
        }
    }

    override fun setReadOnly(readOnly: Boolean) {
        if (!readOnly) throw IllegalArgumentException("Not Writable")
    }

    override fun isReadOnly(): Boolean {
        return true
    }

    override fun setRequiredIndicatorVisible(requiredIndicatorVisible: Boolean) {
        if (requiredIndicatorVisible) throw IllegalArgumentException("Not Writable")
    }

    override fun isRequiredIndicatorVisible(): Boolean {
        return false
    }
}