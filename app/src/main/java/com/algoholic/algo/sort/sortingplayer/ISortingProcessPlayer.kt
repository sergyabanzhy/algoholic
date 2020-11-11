package com.algoholic.algo.sort.sortingplayer

import com.algoholic.algo.sort.Column

interface ISortingProcessPlayer {
    suspend fun startSorting(started: () -> (Unit), sorted: () -> (Unit), invalidate: () -> Unit)
    fun stopSorting(stopped: () -> (Unit))
    fun stepForward()
    fun stepBackward()
    fun collectionToVisualize(columns: MutableList<Column>)
}