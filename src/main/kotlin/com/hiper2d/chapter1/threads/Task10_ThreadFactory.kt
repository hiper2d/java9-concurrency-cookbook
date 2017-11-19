package com.hiper2d.chapter1.threads

import java.time.LocalDateTime
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit


class MyThreadFactory(var name: String): ThreadFactory {
    private var counter: Int = 0
    private var stats = ArrayList<String>()

    override fun newThread(r: Runnable): Thread {
        val tr = Thread(r, "$name Thread_$counter")
        counter++
        stats.add("Created thread with id ${tr.id} and name '$name' on ${LocalDateTime.now()}")
        return tr
    }

    fun getStats(): String {
        val sb = StringBuilder()
        stats.forEach { sb.append(it).append("\n") }
        return sb.toString()
    }
}

class AwesomeTask: Runnable {
    override fun run() {
        TimeUnit.SECONDS.sleep(1)
    }
}

fun main(args: Array<String>) {
    val factory = MyThreadFactory("AwesomeFactory")
    val task = AwesomeTask()

    println("Starting threads")
    (1..10).forEach { factory.newThread(task).start() }

    println("Factory stats:")
    println(factory.getStats())
}