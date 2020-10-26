package com.algoholic.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.rgb
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*


class FieldView : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    private val TAG = "FieldView"
    private val colorBackground = rgb(188, 245, 204)
    private var initialized: Boolean = false
    private val board: MutableMap<Int, LinkedList<Vertex>> = mutableMapOf()
    private var gcd = -1
    private val initialVertexesToDraw: MutableList<Vertex> = mutableListOf()
    val mainHandler = Handler(Looper.getMainLooper())
    private val gridColor = Paint().apply {
        color = Color.RED
        strokeWidth = 3F
    }

    private val path: MutableList<Vertex?> = mutableListOf()

    private var found: Boolean = false

    private val textColor = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3F
        textSize = 20F
    }

    private val listener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

    private val detector: GestureDetector = GestureDetector(context, listener)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(TAG, "onLayout")
        super.onLayout(changed, left, top, right, bottom)

        Log.d(TAG, "height - $height")
        Log.d(TAG, "width - $width")
        gcd = gcd(height, width)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun drawYLines(canvas: Canvas) {
        for (x in 0 until canvas.width step gcd) {
            canvas.drawLine(x.toFloat(), 0F, x.toFloat(), canvas.height.toFloat(), gridColor)
            if (!initialized) {
                val key = if (x > 0) x / gcd else x
                board[key] = LinkedList()
            }

        }
    }

    private fun drawBoard(canvas: Canvas) {
        for (y in 0 until canvas.height step gcd) {
            canvas.drawLine(0F, y.toFloat(), canvas.width.toFloat(), y.toFloat(), gridColor)

            for (x in 0 until canvas.width step gcd) {
                canvas.drawLine(x.toFloat(), 0F, x.toFloat(), canvas.height.toFloat(), gridColor)
            }
        }
    }

    private fun drawXLines(canvas: Canvas) {
        for (y in 0 until canvas.height step gcd) {
            canvas.drawLine(0F, y.toFloat(), canvas.width.toFloat(), y.toFloat(), gridColor)
            if (!initialized) {
                for (v in 0 until canvas.height step gcd) {
                    addByCell(y, Vertex(x = y, y = v, edge = gcd))
                }
            }
        }
    }

    private fun addByCell(key: Int, value: Vertex) {
        val bucket = if (key > 0) key / gcd else key
        value.bucket = bucket
        board[bucket]?.add(value)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")

        prepareField(canvas)

        initialVertexesToDraw.forEach {
            canvas.drawRect(
                it.rect(),
                it.paint.paint
            )
        }

        visitedVertices.forEach {
            canvas.drawRect(
                it.rect(),
                it.paint.paint
            )
        }

        visitedVertices.forEach {
            canvas.drawText(
                it.distance.toString(),
                (it.x.toFloat() + gcd / 2),
                (it.y.toFloat() + gcd / 2),
                textColor
            )
        }

        path.forEach {
            it?.run {
                canvas.drawRoundRect(
                    it.rect(),
                    radius.toFloat(),
                    radius.toFloat(),
                    it.paint.paint
                )

                canvas.drawText(
                    it.distance.toString(),
                    (it.x.toFloat() + gcd / 2),
                    (it.y.toFloat() + gcd / 2),
                    gridColor
                )
            }
        }

        drawBoard(canvas)
    }

    private fun prepareField(canvas: Canvas) {
        canvas.drawColor(colorBackground)
        drawYLines(canvas)
        drawXLines(canvas)
        initialized = true
    }

    internal val visitedVertices: MutableList<Vertex> = mutableListOf()

    fun start() {
        star()
    }

    private fun star() {
        if (found) {
            return
        }

        mainHandler.post(object : Runnable {
            override fun run() {

                if (found) {
                    mainHandler.removeCallbacksAndMessages(null)
                    invalidate()
                    return
                } else {
                    mainHandler.postDelayed(this, 100)
                }
                
                dijcstra()
            }
        })
    }

    internal fun findCellWithShortestDistance(): Vertex? {

        return if (visitedVertices.isEmpty()) initialVertexesToDraw.first() else visitedVertices.filterNot { it.disabled }
            .minByOrNull { it.distance }
    }

    internal fun containsEndCell(vertices: MutableList<Vertex>): Boolean {
        vertices.forEach {
            if (!it.isEnd()) {
                it.paint = Marker.Visited()
            } else {
                getPath(it)
            }
        }
        found = vertices.any { it.isEnd() }

        return found
    }

    private fun getPath(endVertex: Vertex) {

        var previousVertex: Vertex? = endVertex.previousVertex

        while (previousVertex?.isStart() == false) {

            val cp = previousVertex.copy()
            cp.paint = Marker.Path()
            path.add(cp)

            previousVertex = previousVertex.previousVertex
        }

        Log.d(TAG, "path  $path")
    }

    internal fun findNeighborsForCell(start: Vertex): MutableList<Vertex> {
        val neighbors = mutableListOf<Vertex>()

        board[start.bucket]?.run {
            neighbors.addAll(getNeighborsInOneBucket(this, start))
        }

        board[(start.bucket - 1)]?.run {
            getFromNearBucket(this, getCellIndexInList(board, start), start.distance)?.run {
                neighbors.add(this)
            }
        }

        board[(start.bucket + 1)]?.run {
            getFromNearBucket(this, getCellIndexInList(board, start), start.distance)?.run {
                neighbors.add(this)
            }
        }

        return neighbors
    }

    private fun getFromNearBucket(nearBucket: LinkedList<Vertex>, index: Int, distance: Int): Vertex? {

        val cell = nearBucket[index]
        return if (!visitedVertices.contains(cell) && !cell.isStart() && !cell.isBlock) {
            cell.distance = if (distance == Int.MAX_VALUE && distance == 0) 1 else distance + 1
            cell
        } else {
            null
        }
    }

    private fun getCellIndexInList(board: MutableMap<Int, LinkedList<Vertex>>, start: Vertex): Int {
        return board[start.bucket]?.run {
            indexOf(start)
        } ?: 0
    }

    private fun getNeighborsInOneBucket(bucket: LinkedList<Vertex>, start: Vertex): MutableList<Vertex> {
        val neighbors = mutableListOf<Vertex>()
        getNext(bucket, start)?.run {
            if (!visitedVertices.contains(this) && !isStart() && !isBlock) {
                distance =
                    if (distance == Int.MAX_VALUE && start.distance == 0) 1 else start.distance + 1
                neighbors.add(this)
            }
        }

        getPrevious(bucket, start)?.run {
            if (!visitedVertices.contains(this) && !isStart() && !isBlock) {
                distance =
                    if (distance == Int.MAX_VALUE && start.distance == 0) 1 else start.distance + 1
                neighbors.add(this)
            }
        }
        return neighbors
    }

    private fun getNext(bucket: LinkedList<Vertex>, start: Vertex): Vertex? {
        val iterator = bucket.listIterator(bucket.indexOf(start).inc())
        return if (iterator.hasNext()) iterator.next() else null
    }

    private fun getPrevious(bucket: LinkedList<Vertex>, start: Vertex): Vertex? {
        val iterator = bucket.listIterator(bucket.indexOf(start))
        return if (iterator.hasPrevious()) iterator.previous() else null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event).let { result ->
            if (!result) {
                if (event.action == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "event - X:${event.x}, Y:${event.y}")
                    findTouchedCell(event.x, event.y)?.run {
                        when {
                            initialVertexesToDraw.isEmpty() -> {
                                distance = 0
                                paint = Marker.SourcePoint()
                            }
                            initialVertexesToDraw.size == 1 -> {
                                paint = Marker.EndPoint()
                            }
                            else -> {
                                isBlock = true
                                paint = Marker.BlockedPoint()
                            }
                        }

                        initialVertexesToDraw.add(this)

                        invalidate()
                    }
                    true
                } else false
            } else true
        }
    }

    private fun findTouchedCell(x: Float, y: Float): Vertex? {
        var vertex: Vertex? = null
        val iterator = board.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            entry.value.find { it.rect().contains(x, y) }?.run {
                vertex = this
            }
        }

        return vertex
    }
}

private fun gcd(a: Int, b: Int): Int {
    var a = a
    var b = b
    while (b != 0) {
        val tmp = a % b
        a = b
        b = tmp
    }
    return a
}