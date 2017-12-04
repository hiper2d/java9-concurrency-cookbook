package com.hiper2d.chapter3.utilities

import java.util.concurrent.CyclicBarrier
import java.util.concurrent.ThreadLocalRandom

class MatrixMock(private val size: Int, length: Int, number: Int) {
    private val data = Array(size) { IntArray(length) }
    private var counter: Int = 0

    init {
        val rand = ThreadLocalRandom.current()
        (0 until size).forEach {
            val i = it
            (0 until length).forEach {
                val j = it
                data[i][j] = rand.nextInt(10)
                if (data[i][j] == number) {
                    counter++
                }
            }
        }
        println("Mock: There are $counter occurrences of $number in generated data.")
    }

    fun getRow(i: Int): IntArray = data[i]
}

class Results(i: Int) {
    val data = IntArray(i) // assume that i is between 0 and size exclusive

    fun setData(position: Int, value: Int) {
        data[position] = value
    }
}

class Searcher(
        private val firstRow: Int,
        private val lastRowExclusive: Int,
        private val mock: MatrixMock,
        private val results: Results,
        private val number: Int,
        private val barrier: CyclicBarrier
): Runnable {
    override fun run() {
        println("${Thread.currentThread().name}: Processing lines from $firstRow to $lastRowExclusive")
        (firstRow until lastRowExclusive).forEach {
            val row = mock.getRow(it)
            val count = row.count { it == number }
            results.setData(it, count)
        }
        println("${Thread.currentThread().name}: Lines processed.")
        barrier.await()
    }
}

class Grouper(private val results: Results): Runnable {
    override fun run() {
        println("Grouper: Processing results...")
        println("Grouper: Total result: ${results.data.sum()}.")
    }
}

const val ROWS = 10_000
const val NUMBERS = 1_000
const val SEARCH = 5
const val PARTICIPANTS = 5
const val LINES_PARTICIPANT = 2_000

fun main(args: Array<String>) {
    val mock = MatrixMock(ROWS, NUMBERS, SEARCH)
    val results = Results(ROWS)
    val grouper = Grouper(results)
    val barrier = CyclicBarrier(PARTICIPANTS, grouper)

    (0 until PARTICIPANTS).forEach {
        Thread(
                Searcher(
                        it * LINES_PARTICIPANT,
                        (it * LINES_PARTICIPANT) + LINES_PARTICIPANT,
                        mock,
                        results,
                        SEARCH,
                        barrier
                )
        ).start()
    }
    println("Main: The main thread has finished.")
}