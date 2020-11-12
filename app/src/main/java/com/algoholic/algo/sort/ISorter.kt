package com.algoholic.algo.sort

interface ISorter {
    fun stopSort()
    suspend fun sort(sorted: () -> Unit, invalidate: (List<Int>) -> Unit)
    fun stepForward()
}