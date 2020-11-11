package com.algoholic.algo.sort

import android.util.Log
import kotlinx.coroutines.delay

class BubbleSort(var toBeSorted: MutableList<Column> = mutableListOf()) : ISorter {

    private suspend fun bubbleSortWithSteps(columns: MutableList<Column>, compared: () -> Unit, invalidate: () -> Unit) {
        for (pass in 0 until (columns.size - 1)) {

            // A single pass of bubble sort
            for (currentPosition in 0 until (columns.size - pass - 1)) {

                // This is a single step
                if (columns[currentPosition].bottom > columns[currentPosition + 1].bottom) {
                    swapElements(columns[currentPosition], columns[currentPosition + 1])
                } else {
                    Log.d(TAG, "They are in correct order, do not swap them")
                }

                invalidate.invoke()

                delay(1)
            }
        }
        compared.invoke()
    }

    override suspend fun sort(currentFound:(Column) -> (Unit), compared: () -> (Unit), invalidate: () -> Unit) {
        bubbleSortWithSteps(toBeSorted, compared, invalidate)
    }

    private fun swapElements(current: Column, toCompareWithCurrent: Column) {
        val currHeight = current.bottom
        val toCompareWithCurrentHeight = toCompareWithCurrent.bottom

        toCompareWithCurrent.bottom = currHeight
        current.bottom = toCompareWithCurrentHeight
    }

    override fun stepForward() {

    }

    override fun stopSort() {

    }
}

private const val TAG = "BubbleSort"