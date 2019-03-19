package com.hiper2d.chapter1.threads


class ExceptionHandler: Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        println("An exception has been captured")
        println("Thread: ${t.id}")
        println("Exception: ${e.javaClass.name}, ${e.message}")
        println("Stacktrace:")
        e.printStackTrace()
        println("Thread state: ${t.state}")
    }
}

class Task: Runnable {
    override fun run() {
        "TTT".toInt()
    }
}

fun main() {
    val thr = Thread(Task())
    thr.uncaughtExceptionHandler = ExceptionHandler()
    thr.start()
}