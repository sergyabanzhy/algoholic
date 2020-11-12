package com.algoholic.algo.sort

import android.animation.Animator
import android.graphics.Canvas

interface IPaintable {
    fun draw(canvas: Canvas, index: Int)
    fun animator(invalidate: () -> Unit): Animator
}