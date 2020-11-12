package com.algoholic.algo.sort

interface ISorter {
    fun stopSort()
    suspend fun sort(sorted: () -> Unit, invalidate: suspend (Pair<Column, Column>) -> Unit)
    fun stepForward()
}