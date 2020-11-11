package com.algoholic.algo.sort.sortingplayer

import android.util.Log
import com.algoholic.algo.sort.BubbleSort
import com.algoholic.algo.sort.Column

class SortingProcessPlayer (private val bubbleSort: BubbleSort) : ISortingProcessPlayer {

    override fun collectionToVisualize(columns: MutableList<Column>) {
        Log.d(TAG, "collectionToVisualize")

        bubbleSort.toBeSorted = columns
    }

    override suspend fun startSorting(started: () -> (Unit), sorted: () -> (Unit), invalidate: () -> Unit) {
        Log.d(TAG, "startSorting")

        started.invoke()

        bubbleSort.sort({ it.isAbutting = true }, sorted, invalidate)
    }

    override fun stopSorting(stopped: () -> Unit) {
        Log.d(TAG, "stopSorting")
        stopped.invoke()
        bubbleSort.stopSort()
    }

    override fun stepForward() {
        Log.d(TAG, "stepForward")
        bubbleSort.stepForward()
    }

    override fun stepBackward() {
        Log.d(TAG, "stepBackward")
    }
}

private const val TAG = "SortingProcessPlayer"