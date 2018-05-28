package com.hiper2d.chapter7.collections.task7.atomic

private class Company(val account: Account): Runnable {
    override fun run() {
        (1..10_000).forEach { account.addAmount(1000) }
    }
}

private class Bank(val account: Account): Runnable {
    override fun run() {
        (1..10_000).forEach { account.substractAmount(1000) }
    }
}

fun main(args: Array<String>) {
    val account = Account(1000)
    val bank = Bank(account)
    val company = Company(account)
    println("Account: initial balance ${account.balance}")

    starTransferAndWait(bank, company)

    println("Account: final balance ${account.balance}")
    println("Account: number of operations ${account.operationsCount}")
    println("Account: commission ${account.commission}")
}

private fun starTransferAndWait(bank: Bank, company: Company) {
    val jobs = listOf(Thread(bank), Thread(company))
    jobs.forEach { it.start() }
    jobs.forEach { it.join() }
}
