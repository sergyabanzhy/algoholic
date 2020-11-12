package com.algoholic.algo.sort

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.animation.AccelerateDecelerateInterpolator

data class Column(var left : Int, var top : Int, var right : Int, var bottom : Int, var state: ColumnState = ColumnState.Idle()) : IPaintable {

    private val rect = Rect()

    private var animatedHeight = bottom

    private fun rect(): Rect {
        rect.set(left, top, right, bottom)
        return rect
    }

    override fun draw(canvas: Canvas, index: Int) {
        canvas.drawRect(rect(), state.colorState)
    }

    override fun animator(invalidate: () -> Unit): Animator {
        return createAnimator {
            invalidate.invoke()
        }
    }

    private fun createAnimator(invalidate : () -> Unit): Animator {
        val propertyHeight: PropertyValuesHolder = PropertyValuesHolder.ofInt("PROPERTY_HEIGHT", 0, bottom)
        val animator = ValueAnimator()
        animator.setValues(propertyHeight)
        animator.duration = 200
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            animatedHeight = it.getAnimatedValue("PROPERTY_HEIGHT") as Int
            invalidate.invoke()
        }
      //  animator.start()

        return animator
    }

    sealed class ColumnState {
        abstract val colorState: Paint

        data class Idle(override val colorState: Paint = Paint().apply {
            color = Color.RED
            strokeWidth = 3F
        }): ColumnState()

        data class Abutting(override val colorState: Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 3F
        }): ColumnState()

        data class Comparing(override val colorState: Paint = Paint().apply {
            color = Color.BLUE
            strokeWidth = 3F
        }): ColumnState()
    }
}