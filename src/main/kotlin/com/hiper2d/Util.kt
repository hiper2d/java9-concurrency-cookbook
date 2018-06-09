package com.hiper2d

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ThreadLocalRandom

fun Long.isPrime(): Boolean {
    if (this <= 2) {
        return true
    }
    return (2 until this).none { this.rem(it) == 0L }
}

fun Int.mid(start: Int): Int = (this + start) / 2

fun printThreadInfo(logger: Logger, thread: Thread, state: Thread.State?) {
    logger.info("Main: Id ${thread.id}, name ${thread.name}")
    logger.info("Main: Priority ${thread.priority}")
    logger.info("Main: Old state $state, new state ${thread.state}")
    logger.info("*************************************************")
}

inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)
fun logger(path: String): Logger = LoggerFactory.getLogger(path)

fun randomLocalDate(): LocalDate {
    val startDay = LocalDate.of(1970, 1, 1).toEpochDay()
    val endDay = LocalDate.now().toEpochDay()
    val randomEpochDay = ThreadLocalRandom.current().nextLong(startDay, endDay)
    return LocalDate.ofEpochDay(randomEpochDay)
}