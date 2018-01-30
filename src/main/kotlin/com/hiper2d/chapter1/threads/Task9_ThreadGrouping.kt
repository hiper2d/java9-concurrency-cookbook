package com.hiper2d.chapter1.threads

import java.util.concurrent.ThreadLocalRandom


class MyThreadGroup(name: String): ThreadGroup(name) {

    override fun uncaughtException(t: Thread, e: Throwable) {
        println("Thread ${t.id} has thrown an exception")
        e.printStackTrace()
        println("Terminating all other theads in the group")
        interrupt()
    }
}

class TaskWithError: Runnable {

    override fun run() {
        val rand = ThreadLocalRandom.current()
        while (true) {
            1000 / rand.nextInt(1_000_000_000)
            if (Thread.interrupted()) {
                println("Thread ${Thread.currentThread().id} was interrupted")
                return
            }
        }
    }
}

fun main(args: Array<String>) {
    val threadGroup = MyThreadGroup("MyThreadGroup")
    val threadsCount = Runtime.getRuntime().availableProcessors() * 2
    val task = TaskWithError()
    (1..threadsCount).forEach {
        Thread(threadGroup, task).start()
    }

    println("Number of threads ${threadGroup.activeCount()}")
    println("Information about all threads:")
    threadGroup.list()

    val threads = Array<Thread?>(threadGroup.activeCount()) { null }
    threadGroup.enumerate(threads)
    threads.forEach {
        println("Thread ${it?.name} has state ${it?.state}")
    }
}