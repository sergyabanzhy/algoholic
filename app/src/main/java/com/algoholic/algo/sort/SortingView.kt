package com.algoholic.algo.sort

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SortingView : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    private val gridColor = Paint().apply {
        color = Color.RED
        strokeWidth = 3F
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        super.onDraw(canvas)

        canvas.drawRect(Column().rect(0), gridColor)
        canvas.drawRect(Column().rect(1), gridColor)
        canvas.drawRect(Column().rect(2), gridColor)
    }
}

private const val TAG = "SortingView"