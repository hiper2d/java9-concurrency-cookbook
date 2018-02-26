package com.hiper2d.chapter7.collections

import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.util.concurrent.LinkedBlockingDeque

private class Client(val list: LinkedBlockingDeque<String>): Runnable {
    override fun run() {
        (1..3).forEach {
            val i = it
            (1..5).forEach {
                val j = it
                val item = "$i:$j"
                list.put(item)
                println("Client: added $item at ${LocalDateTime.now()}")
            }
            sleep(2_000)
        }
    }
}

fun main(args: Array<String>) {
    val list = LinkedBlockingDeque<String>(3)
    Thread(Client(list)).start()

    (1..5).forEach {
        val i = it
        (1..3).forEach {
            val j = it
            val item = list.take()
            println("Main: removed $item at ${LocalDateTime.now()}")
        }
        sleep(300)
    }

    println("Main: end")
}