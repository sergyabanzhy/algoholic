package com.algoholic.algo

import android.graphics.*


data class Vertex(
    var bucket: Int = 0,
    var edge: Int,
    val x: Int, val y: Int = 0,
    var paint: Appearance = Appearance.Idle(),
    var distance: Int = Int.MAX_VALUE,
    var calculated: Boolean = false,
    var isWall: Boolean = false,
    var previousVertex: Vertex? = null
) {
    val rect: RectF by lazy {
        RectF(x.toFloat(), y.toFloat(), (x + edge).toFloat(), (y + edge).toFloat())
    }
}

fun Vertex.isStart(): Boolean {
    return paint is Appearance.SourcePoint
}

fun Vertex.isEnd(): Boolean {
    return paint is Appearance.EndPoint
}

private fun Vertex.h(goalVertex: Vertex): Int {
    return kotlin.math.abs(x - goalVertex.x) + kotlin.math.abs(y - goalVertex.y)
}

fun Vertex.f(goalVertex: Vertex): Int {
    return if (isStart()) 0 else h(goalVertex) + distance
}

sealed class Appearance {
    abstract val paint: Paint
    data class SourcePoint(override val paint: Paint = Paint().apply { color = Color.GREEN }): Appearance()
    data class EndPoint(override val paint: Paint = Paint().apply { color = Color.YELLOW }): Appearance()
    data class BlockedPoint(override val paint: Paint = Paint().apply { color = Color.BLACK }): Appearance()
    data class Path(override val paint: Paint = Paint().apply { color = Color.CYAN }): Appearance()
    data class Idle(override val paint: Paint = Paint().apply { color = Color.MAGENTA }): Appearance()
    data class Visited(override val paint: Paint = Paint().apply { color = Color.BLUE }): Appearance()
}