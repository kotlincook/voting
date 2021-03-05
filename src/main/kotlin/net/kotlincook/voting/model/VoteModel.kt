package net.kotlincook.voting.model

import net.kotlincook.voting.model.Attitude.*

val options = listOf(Option("Das Meeting startet um 8 Uhr."),
                     Option("Das Meeting startet um 16 Uhr."))

object ModelSingleton : VoteModel(options)

enum class Attitude {
    YES, OK, NO
}

class AttitudeCounter: HashMap<Attitude, Int>() {

    init {
        this[OK] = 0
        this[NO] = 0
        this[YES] = 0
    }

    fun add(attitude: Attitude) {
        this[attitude] = this[attitude]!! + 1
    }

    override fun toString(): String {
        return "yes = ${this[YES]}, irr = ${this[OK]}, no = ${this[NO]}"
    }
}


data class Option(val descripion: String, val arguments: MutableList<String> = ArrayList()) {
    override fun equals(other: Any?) = this === other
    override fun hashCode() = System.identityHashCode(this)
}

open class VoteModel(val options: List<Option> = ArrayList()) {
    constructor(option: Option): this(listOf(option))

    val votesPerOption: MutableMap<Option, AttitudeCounter> =
            HashMap<Option, AttitudeCounter>().apply {
                options.forEach { put(it, AttitudeCounter()) }
            }

    fun addAttitude(option: Option, attitude: Attitude) {
        var votes = votesPerOption[option]

        if (votes == null) {
            votesPerOption[option] = AttitudeCounter()
        }
        votesPerOption[option]!!.add(attitude)
    }

    override fun toString() =
        votesPerOption.toList()
                .fold("VoteModel: ", {s, pair -> s + "[${pair.first}, Attitudes: ${pair.second}],"})
                .substringBeforeLast(",")

}