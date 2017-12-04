package com.hiper2d.chapter3.utilities

import java.lang.Thread.sleep
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadLocalRandom

class Videoconference(count: Int): Runnable {

    private val controller = CountDownLatch(count)

    override fun run() {
        println("VideoConference: Initialization: ${controller.count} participants.")
        controller.await()
        println("VideoConference: All the participants have come.")
        println("VideoConference: Let's start...")
    }

    fun arrive(name: String) {
        println("$name has arrived.")
        controller.countDown()
        println("VideoConference: Waiting for ${controller.count} participants.")
    }
}

class Participant(private val conference: Videoconference, private val name: String): Runnable {
    override fun run() {
        sleep(ThreadLocalRandom.current().nextLong(10))
        conference.arrive(name)
    }
}

fun main(args: Array<String>) {
    val conference = Videoconference(10)
    Thread(conference).start()

    (1..10).forEach {
        Thread(Participant(conference, "Participant $it")).start()
    }
}