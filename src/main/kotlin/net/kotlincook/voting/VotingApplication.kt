package net.kotlincook.voting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VotingApplication

fun main(args: Array<String>) {
	runApplication<VotingApplication>(*args)
}

