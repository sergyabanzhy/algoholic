package com.algoholic.algo.sort

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

data class Column(var left : Int, var top : Int, var right : Int, var bottom : Int, var isAbutting: Boolean = false) : IPaintable {

    private val rect = Rect()

    private fun rect(): Rect {
        rect.set(left, top, right, bottom)
        return rect
    }

    private val color = Paint().apply {
        color = Color.RED
        strokeWidth = 3F
    }

    private val colorActive = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3F
    }

    override fun draw(canvas: Canvas, index: Int) {
        canvas.drawRect(rect(), if (isAbutting) colorActive else color)
    }
}