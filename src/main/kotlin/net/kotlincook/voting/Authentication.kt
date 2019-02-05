package net.kotlincook.voting

interface Authentication {
    fun isCodeValid(code: String): Boolean
}

object Authenticator: Authentication {

    override fun isCodeValid(code: String): Boolean {
        return true
    }
}