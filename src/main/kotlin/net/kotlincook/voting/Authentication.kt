package net.kotlincook.voting

import net.kotlincook.voting.Authentication.AuthResult.*
import net.kotlincook.voting.Authenticator.MAGIC1
import net.kotlincook.voting.Authenticator.MAGIC2
import net.kotlincook.voting.Authenticator.TIME_DIVISOR
import java.text.SimpleDateFormat

interface Authentication {
    enum class AuthResult { VALID, USED, EXPIRED, INVALID}
    fun isCodeValid(code: String?): AuthResult
}

object Authenticator: Authentication {

    val TIME_DIVISOR = 1000L
    val MAGIC1 = 2937L
    val MAGIC2 = 1311L
    val usedCodes =  HashSet<String>()

    override fun isCodeValid(code: String?) =
        when {
            code == null -> INVALID
            usedCodes.contains(code) -> USED
            else -> {
                val codeAsLong = try {
                    code.toLong()
                } catch (e: NumberFormatException) {
                    0L
                }
                when {
                    !codePreValidation(codeAsLong) -> INVALID
                    !codeTimeValidation(codeAsLong) -> EXPIRED
                    else -> {
                        usedCodes += code
                        VALID
                    }
                }
            }
        }

    private fun codePreValidation(code: Long) = code % MAGIC1 == MAGIC2

    private fun codeTimeValidation(code: Long): Boolean {
        return (code - MAGIC2) / MAGIC1 > System.currentTimeMillis() / TIME_DIVISOR
    }
}

fun main1(args: Array<String>) {
    val parse = SimpleDateFormat("yyyy-MM-dd").parse("2222-03-16")
    val set: MutableSet<Int> = mutableSetOf()
    for (i in 1..30) {
        set += (Math.random() * TIME_DIVISOR).toInt()
    }
    val list = ArrayList(set)
    list.sort()
    list.forEach {
        println((parse.time/TIME_DIVISOR + it) * MAGIC1 + MAGIC2)
    }
}
