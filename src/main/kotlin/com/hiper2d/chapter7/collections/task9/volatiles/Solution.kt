package com.hiper2d.chapter7.collections.task9.volatiles

import com.hiper2d.logger
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.util.concurrent.TimeUnit

val log = logger("com.hiper2d.chapter7.collections.task9.volatiles")

fun runWithCoroutines() {
    val flag = Flag()
    val volatileFLag = VolatileFlag()

    val task = Task(flag)
    val volatileTask = VolatileTask(volatileFLag)

    async(CommonPool) {
        task.run()
        volatileTask.run()
    }
    TimeUnit.SECONDS.sleep(1)

    stopVolatileTask(volatileFLag)
    TimeUnit.SECONDS.sleep(1)

    stopTask(flag)
    TimeUnit.SECONDS.sleep(1)
}

fun runWithThreads() {
    val flag = Flag()
    val volatileFLag = VolatileFlag()

    val task = Task(flag)
    val volatileTask = VolatileTask(volatileFLag)

    startBothTasks(task, volatileTask)
    TimeUnit.SECONDS.sleep(1)

    stopVolatileTask(volatileFLag)
    TimeUnit.SECONDS.sleep(1)

    stopTask(flag)
    // It is expected that the Task will never recognize flag changes and won't stop.
}

private fun startBothTasks(task: Task, volatileTask: VolatileTask) {
    val thread = Thread(task)
    val volatileThread = Thread(volatileTask)
    arrayOf(thread, volatileThread).forEach { it.start() }
}

private fun stopTask(flag: Flag) {
    log.debug("Going to stop normal task")
    flag.flag = false
    log.debug("Stop flag changed")
}

private fun stopVolatileTask(volatileFLag: VolatileFlag) {
    log.debug("Going to stop volatile task")
    volatileFLag.flag = false
    log.debug("Volatile stop flag changed")
}

fun main(args: Array<String>) {
    // runWithThreads()
    runWithCoroutines()
}