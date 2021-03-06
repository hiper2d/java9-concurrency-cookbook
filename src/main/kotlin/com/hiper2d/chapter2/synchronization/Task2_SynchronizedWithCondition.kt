package com.hiper2d.chapter2.synchronization

import java.time.LocalDateTime
import java.util.*

class EventStorage (
        private val maxSize: Int = 10,
        private val storage: Queue<LocalDateTime> = LinkedList()
): Object() {

    @Synchronized fun set() {
        while (storage.size == maxSize) {
            wait()
        }
        storage.offer(LocalDateTime.now())
        println("Set a date, storage size: ${storage.size}")
        notify()
    }

    @Synchronized fun get() {
        while (storage.size == 0) {
            wait()
        }
        println("Get a date '${storage.poll()}', storage size: ${storage.size}")
        notify()
    }
}

private class ConditionProducer(private val storage: EventStorage): Runnable {
    override fun run() {
        (1..100).forEach { storage.set() }
    }
}

private class ConditionConsumer(private val storage: EventStorage): Runnable {
    override fun run() {
        (1..100).forEach { storage.get() }
    }
}

fun main(args: Array<String>) {
    val storage = EventStorage()
    Thread(ConditionProducer(storage)).start()
    Thread(ConditionConsumer(storage)).start()
}