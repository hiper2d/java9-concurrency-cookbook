package com.hiper2d.chapter4.executors

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class UserValidator(val validatorName: String) {
    fun validate(username: String, passwort: String): Boolean {
        val rand = ThreadLocalRandom.current()
        val duration = rand.nextLong(10)
        println("Validator $validatorName: validating a user during $duration")
        TimeUnit.SECONDS.sleep(duration)
        return rand.nextBoolean()
    }
}

class ValidatorTask(
        private val userValidator: UserValidator,
        private val username: String,
        private val password: String
): Callable<String> {
    override fun call(): String {
        if (!userValidator.validate(username, password)) {
            println("${userValidator.validatorName}: The user '$username' has not been found")
            throw Exception("Error validating user")
        }
        println("${userValidator.validatorName}: The user '$username' has been found")
        return userValidator.validatorName
    }
}

fun main(args: Array<String>) {
    val username = "test"
    val password = "test"

    val ldapValidator = UserValidator("LDAP")
    val dbValidator = UserValidator("Dbatabase")

    val ldapTask = ValidatorTask(ldapValidator, username, password)
    val dbTask = ValidatorTask(dbValidator, username, password)

    val executor = Executors.newCachedThreadPool()
    val result = executor.invokeAny(listOf(ldapTask, dbTask))
    println("Main: result is $result")
    executor.shutdown()

    println("Main: End of the Execution")
}