package net.kotlincook.voting.model

import net.kotlincook.voting.model.Attitude.*

val options = listOf(Option("Wir möchten in Zukunft per Konsent-Beschluss abstimmen."),
                     Option("Wir wollen so weitermachen wie bislang."))

object ModelSingleton : VoteModel(options)

enum class Attitude {
    YES, IRR, NO
}

class AttitudeCounter: HashMap<Attitude, Int>() {

    init {
        this[IRR] = 0
        this[NO] = 0
        this[YES] = 0
    }

    fun add(attitude: Attitude) {
        this[attitude] = this[attitude]!! + 1
    }

    override fun toString(): String {
        return "yes = ${this[YES]}, irr = ${this[IRR]}, no = ${this[NO]}"
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