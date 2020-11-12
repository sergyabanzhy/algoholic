package com.algoholic.algo.sort.sortingplayer

import com.algoholic.algo.sort.Column

interface ISortingProcessPlayer {
    fun stopSorting(stopped: () -> (Unit))
    fun stepForward()
    fun stepBackward()
    fun collectionToVisualize(columns: MutableList<Column>)
    suspend fun startSorting(started: () -> Unit, sorted: () -> Unit, invalidate: suspend (Pair<Column, Column>) -> Unit)
}