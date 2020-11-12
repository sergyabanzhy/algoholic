package com.algoholic.algo.sort

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SortingView: View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    var columns: List<IPaintable> = emptyList()
    set(value) {
        field = value
        invalidate()
        //columns.forEach { it.animate { invalidate() } }
    }

    fun invalidateByIndex(indexes: List<Int>) {
        indexes.forEach {
            columns[it].animate { invalidate() }
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")

        columns.forEachIndexed { index, column ->
            column.draw(canvas, index)
        }
    }
}

private const val TAG = "SortingView"