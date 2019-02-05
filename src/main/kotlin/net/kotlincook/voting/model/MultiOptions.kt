package net.kotlincook.voting.model

var options: MutableSet<Option> = HashSet()

data class Vote(val votePerOption: HashMap<Option, Attitude> = HashMap())

var votes: MutableMap<Attitude, Integer> = HashMap()



