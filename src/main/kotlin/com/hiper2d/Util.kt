package com.hiper2d

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Long.isPrime(): Boolean {
    if (this <= 2) {
        return true
    }
    return (2 until this).none { this.rem(it) == 0L }
}

fun printThreadInfo(logger: Logger, thread: Thread, state: Thread.State?) {
    logger.info("Main: Id ${thread.id}, name ${thread.name}")
    logger.info("Main: Priority ${thread.priority}")
    logger.info("Main: Old state $state, new state ${thread.state}")
    logger.info("*************************************************")
}

inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)