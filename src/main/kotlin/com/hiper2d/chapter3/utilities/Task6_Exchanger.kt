package com.hiper2d.chapter3.utilities

import java.util.concurrent.Exchanger

class ExchangeProducer(
        private var buffer: MutableList<String>,
        private val exchanger: Exchanger<MutableList<String>>
): Runnable {

    override fun run() {
        (1..10).forEach {
            val cycle = it
            println("Producer: Cycle $cycle")
            (0 until 10).forEach {
                val message = "Event: ${(cycle - 1) * 10 + it}"
                println("Producer: $message")
                buffer.add(message)
            }
            buffer = exchanger.exchange(buffer)
            println("Producer: ${buffer.size}")
        }
    }
}

class ExchangeConsumer(
        private var buffer: MutableList<String>,
        private val exchanger: Exchanger<MutableList<String>>
): Runnable {

    override fun run() {
        (1..10).forEach {
            val cycle = it
            println("Consumer: Cycle $cycle")
            buffer = exchanger.exchange(buffer)
            println("Consumer: ${buffer.size}")

            (0 until 10).forEach {
                val message = buffer[0]
                println("Consumer: $message")
                buffer.removeAt(0)
            }
        }
    }
}

fun main(args: Array<String>) {
    val buffer1 = ArrayList<String>()
    val buffer2 = ArrayList<String>()
    val exchanger = Exchanger<MutableList<String>>()

    val producer = ExchangeProducer(buffer1, exchanger)
    val consumer = ExchangeConsumer(buffer2, exchanger)

    Thread(producer).start()
    Thread(consumer).start()
}