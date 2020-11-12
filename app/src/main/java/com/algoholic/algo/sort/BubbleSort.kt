package com.algoholic.algo.sort

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BubbleSort(var toBeSorted: MutableList<Column> = mutableListOf()) : ISorter {

    private suspend fun bubbleSortWithSteps(columns: MutableList<Column>, compared: () -> Unit, invalidate: (List<Int>) -> Unit) {
        for (pass in 0 until (columns.size - 1)) {

            for (currentPosition in 0 until (columns.size - pass - 1)) {

                highLightCurrentPare(columns[currentPosition], columns[currentPosition + 1])

                if (columns[currentPosition].bottom > columns[currentPosition + 1].bottom) {
                    swapElements(columns[currentPosition], columns[currentPosition + 1], invalidate)
                } else {
                    Log.d(TAG, "They are in correct order, do not swap")

                    delay(DELAY)
                    suspendCoroutine {
                        Log.d(TAG, ">> this is blocking run ")
                        columns[currentPosition].state = Column.ColumnState.Abutting()
                        columns[currentPosition + 1].state = Column.ColumnState.Comparing()

                        invalidate.invoke(listOf(currentPosition, currentPosition + 1))

                        columns[currentPosition].state = Column.ColumnState.Idle()
                        columns[currentPosition + 1].state = Column.ColumnState.Idle()

                        invalidate.invoke(listOf(currentPosition, currentPosition + 1))
                        Log.d(TAG, "<< this is blocking run ")

                        it.resume(Unit)
                    }
                }
            }
        }
        compared.invoke()
    }

    override suspend fun sort(sorted: () -> Unit, invalidate: (List<Int>) -> Unit) {
        delay(DELAY)
        bubbleSortWithSteps(toBeSorted, sorted, invalidate)
    }

    private suspend fun highLightCurrentPare(current: Column, toCompareWithCurrent: Column) {
        suspendCoroutine<Unit> {
            current.state = Column.ColumnState.Abutting()
            toCompareWithCurrent.state = Column.ColumnState.Comparing()

            it.resume(Unit)
        }
    }

    private suspend fun swapElements(current: Column, toCompareWithCurrent: Column, invalidate: (List<Int>) -> Unit) {
        Log.d(TAG, "swapElements")

        delay(DELAY)
        suspendCoroutine<Unit> {
            Log.d(TAG, ">> swapElements this is blocking run ")
            val currHeight = current.bottom
            val toCompareWithCurrentHeight = toCompareWithCurrent.bottom

            toCompareWithCurrent.bottom = currHeight
            current.bottom = toCompareWithCurrentHeight

            current.state = Column.ColumnState.Idle()
            toCompareWithCurrent.state = Column.ColumnState.Idle()

            invalidate.invoke(listOf(toBeSorted.indexOf(current), toBeSorted.indexOf(toCompareWithCurrent)))

            Log.d(TAG, "<< swapElements this is blocking run ")

            it.resume(Unit)
        }

        Log.d(TAG, "!!!swapElements blocking OUTER")
    }

    override fun stepForward() {
        Log.d(TAG, "stepForward")
    }

    override fun stopSort() {
        Log.d(TAG, "stopSort")
    }
}

private const val TAG = "BubbleSort"
private const val DELAY = 10L