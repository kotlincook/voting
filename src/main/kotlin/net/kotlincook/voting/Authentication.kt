package net.kotlincook.voting

import net.kotlincook.voting.Authentication.AuthResult.*

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
                usedCodes += code
                if (codeValidation(code)) OK else INVALID
            }
        }

    private fun codeValidation(code: String) = code.hashCode() % 2 == 0
}