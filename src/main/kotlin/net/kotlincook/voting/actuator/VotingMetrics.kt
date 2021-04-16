package net.kotlincook.voting.actuator

import io.micrometer.core.instrument.Counter
import org.springframework.stereotype.Component

import io.micrometer.core.instrument.MeterRegistry


@Component
class VotingMetrics(meterRegistry: MeterRegistry) {
    val votingCounter: Counter = meterRegistry.counter("voting_counter")

    fun countVoting() {
        votingCounter.increment()
    }

}