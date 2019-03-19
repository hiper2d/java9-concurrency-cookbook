package com.hiper2d.chapter1.threads

import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit


class WriterTask(val deque: Deque<Event>): Runnable {
    override fun run() {
        (1..10).forEach {
            val event = Event(LocalDateTime.now())
            println("THe thread ${Thread.currentThread().id} has generate an event")
            deque.addFirst(event)
            TimeUnit.SECONDS.sleep(1)
        }
    }
}

class CleanerTask(val deque: Deque<Event>): Thread() {
    init {
        isDaemon = true
    }

    override fun run() {
        while (true) {
            var deleted = false
            while (!deque.isEmpty() && LocalDateTime.now().minusSeconds(5) > (deque.last.date)) {
                val evt = deque.removeLast()
                deleted = true
                println("Cleaner: deleted event ${evt.date}")
            }
            if (deleted) {
                println("Cleaner: deque size ${deque.size}")
            }
        }
    }
}

class Event(val date: LocalDateTime)

fun main() {
    val deque = ConcurrentLinkedDeque<Event>()
    repeat(Runtime.getRuntime().availableProcessors()) {
        Thread(WriterTask(deque)).start()
    }
    CleanerTask(deque).start()
}