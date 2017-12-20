package com.hiper2d.chapter5.forkjoin

import java.util.concurrent.RecursiveTask
import java.util.concurrent.ThreadLocalRandom

class DocumentMock {
    private val words = arrayOf("the", "hello", "goodbye", "packt", "java", "thread", "pool", "random", "class", "main")

    fun generateDocument(linesNum: Int, wordsInLineNum: Int, searchWord: String): Array<Array<String>> {
        var counter = 0
        val rand = ThreadLocalRandom.current()
        val document = (1..linesNum).map {
            (1..wordsInLineNum).map {
                val word = words[rand.nextInt(words.size)]
                if (word == searchWord) {
                    counter++
                }
                word
            }.toTypedArray()
        }.toTypedArray()

        println("DocumentMock: The word appears $counter times in the document")
        return document
    }
}

private class DocumentTask(
        private val doc: Array<Array<String>>,
        private val first: Int,
        private val last: Int,
        private val searchWord: String
): RecursiveTask<Int>() {
    override fun compute(): Int {
        // todo: continue here
        return 0
    }
}

fun main(args: Array<String>) {

}