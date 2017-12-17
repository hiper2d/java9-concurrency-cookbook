package com.hiper2d.chapter4.executors

import java.util.concurrent.*

class ReportGenerator(private val sender: String, private val title: String): Callable<String> {
    override fun call(): String {
        val duration = ThreadLocalRandom.current().nextLong(10)
        println("$sender $title: Generating a report during $duration seconds")
        TimeUnit.SECONDS.sleep(duration)
        return "$sender: $title"
    }
}

class ReportRequest(private val name: String, private val service: CompletionService<String>): Runnable {
    override fun run() {
        val reportGenerator = ReportGenerator(name, "Report")
        service.submit(reportGenerator)
    }
}

class ReportProcessor(private val service: CompletionService<String>): Runnable {
    private var end = false

    override fun run() {
        while (!end) {
            checkForCompletedReportAndPrintIt()
        }
        println("ReportSender: End")
    }

    private fun checkForCompletedReportAndPrintIt() {
        val future = service.poll(20, TimeUnit.SECONDS)
        if (future != null) {
            val report = future.get()
            println("ReportReceiver: Report Received: $report")
        }
    }

    fun stopProcessing() {
        end = true
    }
}

fun main(args: Array<String>) {
    val executor = Executors.newCachedThreadPool()
    val service = ExecutorCompletionService<String>(executor)

    val faceRequest = ReportRequest("Face", service)
    val onlineRequest = ReportRequest("Online", service)
    val processor = ReportProcessor(service)

    println("Main: Starting the Threads")
    val faceRequestThread = Thread(faceRequest)
    val onlineRequestThread = Thread(onlineRequest)
    val processorThread = Thread(processor)
    arrayOf(faceRequestThread, onlineRequestThread, processorThread).forEach { it.start() }
    arrayOf(faceRequestThread, onlineRequestThread).forEach { it.join() }

    println("Main: Shutting down the executor.")
    executor.shutdown()
    executor.awaitTermination(1, TimeUnit.DAYS)

    processor.stopProcessing()
    println("Main: Ends")
}