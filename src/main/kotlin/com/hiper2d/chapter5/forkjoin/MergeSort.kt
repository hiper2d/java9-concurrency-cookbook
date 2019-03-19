package com.hiper2d.chapter5.forkjoin

fun mergeSort(arr: Array<Int>) {
    sort(arr, 0, arr.lastIndex)
}

internal fun sort(arr: Array<Int>, start: Int, end: Int) {
    if (start >= end) {
        return
    }
    // sort half
    // sort another half
    // merge
}

internal fun merge(arr: Array<Int>, start: Int, end: Int) {

}