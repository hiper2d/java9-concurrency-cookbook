package com.hiper2d.chapter5.forkjoin

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CountedCompleter
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit

private class FolderProcessor: CountedCompleter<List<String>> {
    private val path: Path
    private val extension: String
    private val tasks = ArrayList<FolderProcessor>()
    private val results = ArrayList<String>()

    constructor(path: Path, extension: String): super() {
        this.path = path
        this.extension = extension
    }

    private constructor(countedCompleter: CountedCompleter<List<String>>, path: Path, extension: String): super(countedCompleter) {
        this.path = path
        this.extension = extension
    }

    override fun compute() {
        Files.newDirectoryStream(path).toList().forEach {
            if (Files.isDirectory(it)) {
                forkSubfolderTask(it)
            } else {
                processFile(it)
            }
        }
        if (tasks.size > 50) {
            println("${path.toAbsolutePath()}: ${tasks.size} tasks are run.")
        }
        tryComplete()
    }

    override fun getRawResult(): List<String> {
        return results
    }

    override fun onCompletion(caller: CountedCompleter<*>) {
        tasks.forEach { results.addAll(it.results) }
    }

    private fun forkSubfolderTask(it: Path) {
        val task = FolderProcessor(this, it, extension)
        addToPendingCount(1)
        task.fork()
        tasks.add(task)
    }

    private fun processFile(it: Path) {
        if (checkFile(it.fileName)) {
            results.add(it.toAbsolutePath().toString())
        }
    }

    private fun checkFile(name: Path): Boolean {
        return name.toString().endsWith(extension)
    }
}

fun main(args: Array<String>) {
    val pool = ForkJoinPool.commonPool()

    val dishonoredProcessor = FolderProcessor(Paths.get("D:\\Games\\Dishonored 2"), "exe")
    val firewatchProcessor = FolderProcessor(Paths.get("D:\\Games\\Firewatch"), "txt")
    val anypointProcessor = FolderProcessor(Paths.get("D:\\Soft\\AnypointStudio"), "exe")

    pool.execute(dishonoredProcessor)
    pool.execute(firewatchProcessor)
    pool.execute(anypointProcessor)

    while (!dishonoredProcessor.isDone || !firewatchProcessor.isDone || !anypointProcessor.isDone) {
        println("********************************************")
        println("Main: Active Threads: ${pool.activeThreadCount}")
        println("Main: Task Count: ${pool.queuedTaskCount}")
        println("Main: Steal Count: ${pool.stealCount}")
        TimeUnit.SECONDS.sleep(1)
    }
    pool.shutdown()

    println("games: ${dishonoredProcessor.join().size} files found.")
    println("mongo: ${firewatchProcessor.join().size} files found.")
    println("soft: ${anypointProcessor.join().size} files found.")
}