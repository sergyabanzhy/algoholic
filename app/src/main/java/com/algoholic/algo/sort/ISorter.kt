package com.algoholic.algo.sort

interface ISorter {
    fun stopSort()
    suspend fun sort(currentFound: (Column) -> Unit, compared: () -> Unit, invalidate: () -> Unit)
    fun stepForward()
}