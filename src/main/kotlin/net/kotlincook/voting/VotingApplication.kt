package net.kotlincook.voting

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VotingApplication

fun main(args: Array<String>) {
	val logger = LoggerFactory.getLogger(VotingApplication::class.java)
	logger.info("VotingApplication is starting")
	runApplication<VotingApplication>(*args)
}

