package com.algoholic.algo.sort

import android.util.Log

class BubbleSort(var toBeSorted: MutableList<Column> = mutableListOf()) : ISorter {

    private suspend fun bubbleSortWithSteps(columns: MutableList<Column>, compared: () -> Unit, invalidate: suspend (Pair<Column, Column>) -> Unit) {
        for (pass in 0 until (columns.size - 1)) {

            for (currentPosition in 0 until (columns.size - pass - 1)) {
                invalidate.invoke(Pair(columns[currentPosition], columns[currentPosition + 1]))
                if (columns[currentPosition].bottom > columns[currentPosition + 1].bottom) {
                    swapElements(columns[currentPosition], columns[currentPosition + 1], invalidate)
                } else {
                    Log.d(TAG, "They are in correct order, do not swap")
                    invalidate.invoke(Pair(columns[currentPosition], columns[currentPosition + 1]))
                }
            }
        }

        compared.invoke()
    }

    override suspend fun sort(sorted: () -> Unit, invalidate: suspend (Pair<Column, Column>) -> Unit) {
        bubbleSortWithSteps(toBeSorted, sorted, invalidate)
    }

    private suspend fun swapElements(current: Column, toCompareWithCurrent: Column, invalidate: suspend (Pair<Column, Column>) -> Unit) {
        Log.d(TAG, "swapElements")

            val currHeight = current.bottom
            val toCompareWithCurrentHeight = toCompareWithCurrent.bottom

            toCompareWithCurrent.bottom = currHeight
            current.bottom = toCompareWithCurrentHeight


        invalidate.invoke(Pair(current, toCompareWithCurrent))
    }

    override fun stepForward() {
        Log.d(TAG, "stepForward")
    }

    override fun stopSort() {
        Log.d(TAG, "stopSort")
    }
}

private const val TAG = "BubbleSort"