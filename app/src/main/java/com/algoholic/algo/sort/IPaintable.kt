package com.algoholic.algo.sort

import android.graphics.Canvas

interface IPaintable {
    fun draw(canvas: Canvas, index: Int)
    fun animate(invalidate: () -> Unit)
}