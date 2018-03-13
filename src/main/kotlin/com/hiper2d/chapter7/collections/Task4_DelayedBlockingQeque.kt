package com.hiper2d.chapter7.collections

import java.lang.Thread.sleep
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

private class DelayedEvent(val startDate: LocalDateTime): Delayed {

    override fun compareTo(other: Delayed): Int = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS)).toInt()

    override fun getDelay(unit: TimeUnit): Long = Duration.between(LocalDateTime.now(), startDate).toMillis()
}

private class DelayedTask(private val id: Int, private val queue: DelayQueue<DelayedEvent>): Runnable {
    override fun run() {
        val delay = LocalDateTime.now().plusSeconds(id.toLong())
        println("Thread $id with $delay sec of delay")
        (0..99).forEach {
            val evt = DelayedEvent(delay)
            queue.add(evt)
        }
    }
}

fun main(args: Array<String>) {
    val queue = DelayQueue<DelayedEvent>()
    val threads = Array(5) {
        Thread(DelayedTask(it + 1, queue))
    }

    threads.forEach { it.start() }
    threads.forEach { it.join() }

    do {
        var counter = 0
        while (queue.poll() != null) {
            counter++
        }
        println("At ${LocalDateTime.now()} you have read $counter events")
        sleep(500)
    } while (queue.size > 0)
}