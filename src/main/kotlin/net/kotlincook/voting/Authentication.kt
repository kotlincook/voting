package net.kotlincook.voting

import net.kotlincook.voting.Authentication.AuthResult.*
import java.text.SimpleDateFormat

interface Authentication {
    enum class AuthResult { OK, USED, EXPIRED, INVALID}
    fun isCodeValid(code: String?): AuthResult
}

object Authenticator: Authentication {

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
                        OK
                    }
                }
            }
        }

    private fun codePreValidation(code: Long) = code % 2937L == 1311L

    private fun codeTimeValidation(code: Long): Boolean {
        return (code - 1311L) / 2937L > System.currentTimeMillis() / 100000L
    }
}

fun main(args: Array<String>) {
    val parse = SimpleDateFormat("yyyy-MM-dd").parse("2019-02-10")
    println((parse.time/100000 + 35) * 2937 + 1311)
}
