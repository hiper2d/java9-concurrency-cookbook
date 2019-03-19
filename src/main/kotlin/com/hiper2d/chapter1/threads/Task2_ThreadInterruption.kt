package com.hiper2d.chapter1.threads

import com.hiper2d.isPrime
import java.lang.Thread.sleep

private class PrimeGenerator: Thread() {
    override fun run() {
        var counter = 1L
        while (true) {
            if (counter.isPrime()) {
                println("Number $counter is prime")
            }
            if (isInterrupted) {
                println("The Prime Generator was interrupted")
                return
            }
            counter++
        }
    }
}

fun main() {
    val task: Thread = PrimeGenerator()
    task.start()
    sleep(2000)
    task.interrupt()

    println("Main: Status of the thread ${task.state}")
    println("Main: is interrupted ${task.isInterrupted}")
    println("Main: is alive ${task.isAlive}")

    sleep(1000)

    println("Main: Status of the thread ${task.state}")
    println("Main: is interrupted ${task.isInterrupted}")
    println("Main: is alive ${task.isAlive}")
}

