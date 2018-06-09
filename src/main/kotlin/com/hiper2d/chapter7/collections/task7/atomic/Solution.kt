package com.hiper2d.chapter7.collections.task7.atomic

import com.hiper2d.logger
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking

private val logger = logger("com.hiper2d.chapter7.collections.task7.atomic")

fun runWithThreads() {
    val account = Account(1000)
    val bank = Bank(account)
    val company = Company(account)
    logger.debug("Account: initial balance ${account.balance}")

    starTransferAndWait(bank, company)

    logger.debug("Account: final balance ${account.balance}")
    logger.debug("Account: number of operations ${account.operationsCount}")
    logger.debug("Account: commission ${account.commission}")
}

fun runWithCoroutines() {
    val account = Account(1000)
    logger.debug("Account: initial balance ${account.balance}")

    val pool = newFixedThreadPoolContext(2, "Async")

    runBlocking {
        val bankJob = async(pool) { Bank.runBankTransactions(account) }
        val companyJob = async(pool) { Company.runCompanyTransactions(account) }

        bankJob.join()
        companyJob.join()
        logger.debug("Finish all transactions")
    }

    logger.debug("Account: final balance ${account.balance}")
    logger.debug("Account: number of operations ${account.operationsCount}")
    logger.debug("Account: commission ${account.commission}")
}

private fun starTransferAndWait(bank: Bank, company: Company) {
    val jobs = listOf(Thread(bank), Thread(company))
    jobs.forEach { it.start() }
    jobs.forEach { it.join() }
}

fun main(args: Array<String>) {
    runWithThreads()
    // runWithCoroutines()
}
