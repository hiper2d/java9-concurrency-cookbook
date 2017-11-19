package com.hiper2d.chapter1.threads

import java.util.concurrent.TimeUnit


class ConsoleClock: Runnable {
    override fun run() {
        (1 until 10).forEach {
            try {
                TimeUnit.SECONDS.sleep(1)
                println("Tick")
            } catch (e: InterruptedException) {
                println("The Clock Thread was interrupted")
                forEach@return
            }
        }
    }
}

fun main(args: Array<String>) {
    val task = Thread(ConsoleClock())
    task.start()
    TimeUnit.SECONDS.sleep(5)
    task.interrupt()
}