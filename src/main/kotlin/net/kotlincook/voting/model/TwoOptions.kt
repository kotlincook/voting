package net.kotlincook.voting.model


data class TextFieldBean(var label: String = "", var value: String = "")

val textFieldBean = TextFieldBean("Label", "Value")

val proOption = Option("Auslieferung", mutableListOf("Argument1", "Arguement2"))
val contraOption = Option("keine Auslieferung", mutableListOf("Argument3", "Arguement4"))

