package com.hiper2d.chapter5.forkjoin

import java.lang.Thread.sleep
import java.util.concurrent.*

private const val LINE_NUMBER_THRESHOLD = 10
private const val WORD_NUMBER_TRESHOLD = 100

class DocumentMock {
    companion object {
        private val words = arrayOf("the", "hello", "goodbye", "packt", "java", "thread", "pool", "random", "class", "main")

        fun generateDocument(linesNum: Int, wordsInLineNum: Int, searchWord: String): Array<Array<String>> {
            var counter = 0
            val rand = ThreadLocalRandom.current()
            val document = (0 until linesNum).map {
                (0 until wordsInLineNum).map {
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
}

private class DocumentTask(
        private val doc: Array<Array<String>>,
        private val start: Int,
        private val end: Int,
        private val searchWord: String
): RecursiveTask<Int>() {
    override fun compute(): Int {
        return when {
            end - start < LINE_NUMBER_THRESHOLD -> processLines(doc, start, end, searchWord)
            else -> {
                val mid = (start + end) / 2
                val task1 = DocumentTask(doc, start, mid, searchWord)
                val task2 = DocumentTask(doc, mid, end, searchWord)
                ForkJoinTask.invokeAll(task1, task2)
                groupResults(task1.get(), task2.get())
            }
        }
    }

    private fun processLines(doc: Array<Array<String>>, start: Int, end: Int, searchWord: String): Int {
        val tasks = (start until end).map { LineTask(doc[it], 0, doc[it].size, searchWord) }.toList()
        invokeAll(tasks)
        return tasks.map { it.get() }.sum()
    }
}

private class LineTask(
        private val line: Array<String>,
        private val start: Int,
        private val end: Int,
        private val searchWord: String
): RecursiveTask<Int>() {
    override fun compute(): Int {
        return when {
            end - start < WORD_NUMBER_TRESHOLD -> count(line, start, end, searchWord)
            else -> {
                val mid = (end + start) / 2
                val task1 = LineTask(line, start, mid, searchWord)
                val task2 = LineTask(line, mid, end, searchWord)
                invokeAll(task1, task2)
                groupResults(task1.get(), task2.get())
            }
        }
    }

    private fun count(line: Array<String>, start: Int, end: Int, searchWord: String): Int {
        val count = (start until end).filter { line[it] == searchWord}.count()
        sleep(10)
        return count
    }
}

private fun groupResults(val1: Int, val2: Int) = val1 + val2

fun main(args: Array<String>) {
    val lineCount = 100
    val doc = DocumentMock.generateDocument(lineCount, 1_000, "the")
    val documentTask = DocumentTask(doc, 0, lineCount, "the")
    val pool = ForkJoinPool.commonPool()

    runTaskAndWaitForIt(documentTask, pool)

    pool.shutdown()
    pool.awaitTermination(1, TimeUnit.DAYS)
    println("Main: The word appears ${documentTask.get()} times in the document")
}

private fun runTaskAndWaitForIt(documentTask: DocumentTask, pool: ForkJoinPool) {
    pool.execute(documentTask)
    while (!documentTask.isDone) {
        println("***********************************************")
        println("Main: Active Threads: ${pool.activeThreadCount}")
        println("Main: Task Count: ${pool.queuedTaskCount}")
        println("Main: Steal Count: ${pool.stealCount}")
        TimeUnit.SECONDS.sleep(1)
    }
}