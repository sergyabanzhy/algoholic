package com.algoholic.graph

import android.R.attr.radius
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.*


data class Vertex(
    var bucket: Int = 0,
    var edge: Int,
    val x: Int, val y: Int = 0,
    var paint: Marker = Marker.Idle(),
    var distance: Int = Int.MAX_VALUE,
    var disabled: Boolean = false,
    var previousVertex: Vertex? = null
) {
    var animator = ValueAnimator()
    var radius = 0
}

fun Vertex.rect(): RectF {
    return RectF(x.toFloat(), y.toFloat(), (x + edge).toFloat(), (y + edge).toFloat())
}

fun Vertex.isStart(): Boolean {
    return paint is Marker.SourcePoint
}

fun Vertex.isEnd(): Boolean {
    return paint is Marker.EndPoint
}

fun Vertex.isVisited(): Boolean {
    return paint is Marker.Visited
}

fun Vertex.drawWithAnim(invalidate:() -> (Unit)) {
    val propertyRadius: PropertyValuesHolder = PropertyValuesHolder.ofInt(PROPERTY_RADIUS, 0, 150)
    val propertyRotate: PropertyValuesHolder = PropertyValuesHolder.ofInt(PROPERTY_ROTATE, 0, 360)

    animator = ValueAnimator()
    animator.setValues(propertyRadius, propertyRotate)
    animator.duration = 2000
    animator.addUpdateListener { animation ->
        radius = animation.getAnimatedValue(PROPERTY_RADIUS) as Int
        //rotate = animation.getAnimatedValue(PROPERTY_ROTATE) as Int
        invalidate()
    }
    animator.start()
}

internal const val PROPERTY_RADIUS = "PROPERTY_RADIUS"
internal const val PROPERTY_ROTATE = "PROPERTY_ROTATE"

sealed class Marker {
    abstract val paint: Paint
    data class SourcePoint(override val paint: Paint = Paint().apply { color = Color.GREEN }): Marker()
    data class EndPoint(override val paint: Paint = Paint().apply { color = Color.YELLOW }): Marker()
    data class BlockedPoint(override val paint: Paint = Paint().apply { color = Color.BLACK }): Marker()
    data class Path(override val paint: Paint = Paint().apply { color = Color.CYAN }): Marker()
    data class Idle(override val paint: Paint = Paint().apply { color = Color.MAGENTA }): Marker()
    data class Visited(override val paint: Paint = Paint().apply { color = Color.BLUE }): Marker()
}