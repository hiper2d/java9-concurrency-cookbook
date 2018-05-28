package com.hiper2d.chapter7.collections.task7.atomic

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

private fun runCompanyTransactions(account: Account) {
    (1..10_000).forEach { account.addAmount(1000) }
    println("Finish company transactions")
}

private fun runBankTransactions(account: Account) {
    (1..10_000).forEach { account.substractAmount(1000) }
    println("Finish bank transactions")
}

fun main(args: Array<String>) {
    val account = Account(1000)
    println("Account: initial balance ${account.balance}")

    runBlocking {
        val bankJob = async { runBankTransactions(account) }
        val companyJob = async { runCompanyTransactions(account) }

        bankJob.join()
        companyJob.join()
        println("Finish all transactions")
    }

    println("Account: final balance ${account.balance}")
    println("Account: number of operations ${account.operationsCount}")
    println("Account: commission ${account.commission}")
}