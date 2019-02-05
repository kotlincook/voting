package net.kotlincook.voting.model

enum class Attitude {
    YES, IRRELEVANT, NO
}

data class Option(val descripion: String, val arguments: MutableList<String>)