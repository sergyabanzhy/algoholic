package com.algoholic.graph

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
    var isWall: Boolean = false,
    var previousVertex: Vertex? = null
)

fun Vertex.rect(): RectF {
    return RectF(x.toFloat(), y.toFloat(), (x + edge).toFloat(), (y + edge).toFloat())
}

fun Vertex.isStart(): Boolean {
    return paint is Marker.SourcePoint
}

fun Vertex.isEnd(): Boolean {
    return paint is Marker.EndPoint
}

sealed class Marker {
    abstract val paint: Paint
    data class SourcePoint(override val paint: Paint = Paint().apply { color = Color.GREEN }): Marker()
    data class EndPoint(override val paint: Paint = Paint().apply { color = Color.YELLOW }): Marker()
    data class BlockedPoint(override val paint: Paint = Paint().apply { color = Color.BLACK }): Marker()
    data class Path(override val paint: Paint = Paint().apply { color = Color.CYAN }): Marker()
    data class Idle(override val paint: Paint = Paint().apply { color = Color.MAGENTA }): Marker()
    data class Visited(override val paint: Paint = Paint().apply { color = Color.BLUE }): Marker()
}