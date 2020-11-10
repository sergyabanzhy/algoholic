package com.algoholic.algo

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
import android.widget.Toast
import java.util.*


class BoardView : View {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)


    private val colorBackground = rgb(188, 245, 204)
    private val board: MutableMap<Int, LinkedList<Vertex>> = mutableMapOf()
    private var gridSize = pxFromDp(25F)
    private lateinit var startVertex: Vertex
    private lateinit var endVertex: Vertex
    private val initialVertexesToDraw: MutableList<Vertex> = mutableListOf()
    val mainHandler = Handler(Looper.getMainLooper())
    private val gridColor = Paint().apply {
        color = Color.RED
        strokeWidth = 3F
    }

    private val path: MutableList<Vertex?> = mutableListOf()

    var isAstarSelected: Boolean = false
    private var selectedSpeed: Int = 100
    private val dalay: Int = 100

    fun changeSpeed(value: Int) {
        Log.d(TAG, "value - $value")
        selectedSpeed = dalay + value

        Log.d(TAG, "selectedSpeed - $selectedSpeed")
    }

    private var found: Boolean = false

    internal val visitedVertices: MutableList<Vertex> = mutableListOf()

    private val textColor = Paint().apply {
        color = Color.WHITE
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

        Log.d(TAG, "onLayout height - $height")
        Log.d(TAG, "onLayout width - $width")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged")
        super.onSizeChanged(w, h, oldw, oldh)

        Log.d(TAG, "onSizeChanged height - $height")
        Log.d(TAG, "onSizeChanged width - $width")

        prepareField()
    }

    private fun drawYLines() {
        for (x in 0 until width step gridSize) {
            val key = if (x > 0) x / gridSize else x
            board[key] = LinkedList()
        }
    }

    private fun drawXLines() {
        for (y in 0 until height step gridSize) {
            for (v in 0 until height step gridSize) {
                addByCell(y, Vertex(x = y, y = v, edge = gridSize))
            }
        }
    }

    private fun drawBoard(canvas: Canvas) {
        for (y in 0 until height step gridSize) {
            canvas.drawLine(0F, y.toFloat(), width.toFloat(), y.toFloat(), gridColor)
        }

        for (x in 0 until width step gridSize) {
            canvas.drawLine(x.toFloat(), 0F, x.toFloat(), height.toFloat(), gridColor)
        }
    }

    private fun addByCell(key: Int, value: Vertex) {
        val bucket = if (key > 0) key / gridSize else key
        value.bucket = bucket
        board[bucket]?.add(value)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")

        canvas.drawColor(colorBackground)

        initialVertexesToDraw.forEach {
            canvas.drawRect(
                it.rect,
                it.paint.paint
            )
        }

        visitedVertices.forEach {
            canvas.drawRect(
                it.rect,
                it.paint.paint
            )
        }

        visitedVertices.forEach {
            canvas.drawText(
                it.distance.toString(),
                (it.x.toFloat() + gridSize / 2),
                (it.y.toFloat() + gridSize / 2),
                textColor
            )
        }

        path.forEach {
            it?.run {
                canvas.drawRect(
                    it.rect,
                    it.paint.paint
                )

                canvas.drawText(
                    it.distance.toString(),
                    (it.x.toFloat() + gridSize / 2),
                    (it.y.toFloat() + gridSize / 2),
                    gridColor
                )
            }
        }

        drawBoard(canvas)
    }
    
    private fun prepareField() {
        drawYLines()
        drawXLines()
    }

    fun clear() {
        visitedVertices.clear()
        initialVertexesToDraw.clear()
        path.clear()
        found = false
        mainHandler.removeCallbacksAndMessages(null)

        board.clear()

        prepareField()

        invalidate()
    }

    fun start() {
        if (initialVertexesToDraw.isEmpty()) {
            Toast.makeText(context, "Select start and end vertex first", Toast.LENGTH_LONG).show()
        } else {
            execute()
        }
    }

    private fun execute() {
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
                    mainHandler.postDelayed(this, selectedSpeed.toLong())
                }

                if (isAstarSelected) astar() else dijcstra()
            }
        })
    }

    internal fun findCellWithShortestDistance(): Vertex? {

        return if (visitedVertices.isEmpty()) initialVertexesToDraw.first() else visitedVertices.filterNot { it.calculated }
            .minByOrNull { it.distance }
    }

    internal fun findCellWithShortestH(): Vertex? {

        return if (visitedVertices.isEmpty()) startVertex else visitedVertices.filterNot { it.calculated }
            .minByOrNull { it.f(endVertex) }
    }

    internal fun containsEndCell(vertices: MutableList<Vertex>): Boolean {
        vertices.forEach {
            if (!it.isEnd()) {
                it.paint = Appearance.Visited()
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
            cp.paint = Appearance.Path()
            path.add(cp)

            previousVertex = previousVertex.previousVertex
        }

        Log.d(TAG, "path  $path")
    }

    internal fun findNeighborsForCellAndUpdateDistance(current: Vertex): MutableList<Vertex> {
        val neighbors = mutableListOf<Vertex>()

        board[current.bucket]?.run {
            neighbors.addAll(findNeighborsInOneBucketAndUpdateDistance(this, current))
        }

        board[(current.bucket - 1)]?.run {
            findNeighborInNearBucketAndUpdateDistance(this, getCellIndexInList(board, current), current.distance)?.run {
                neighbors.add(this)
            }
        }

        board[(current.bucket + 1)]?.run {
            findNeighborInNearBucketAndUpdateDistance(this, getCellIndexInList(board, current), current.distance)?.run {
                neighbors.add(this)
            }
        }

        return neighbors
    }

    private fun findNeighborInNearBucketAndUpdateDistance(nearBucket: LinkedList<Vertex>, index: Int, distance: Int): Vertex? {

        val cell = nearBucket[index]
        return if (!visitedVertices.contains(cell) && !cell.isStart() && !cell.isWall) {
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

    private fun findNeighborsInOneBucketAndUpdateDistance(bucket: LinkedList<Vertex>, start: Vertex): MutableList<Vertex> {
        val neighbors = mutableListOf<Vertex>()
        getNext(bucket, start)?.run {
            if (!visitedVertices.contains(this) && !isStart() && !isWall) {
                distance =
                    if (distance == Int.MAX_VALUE && start.distance == 0) 1 else start.distance + 1
                neighbors.add(this)
            }
        }

        getPrevious(bucket, start)?.run {
            if (!visitedVertices.contains(this) && !isStart() && !isWall) {
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
                                startVertex = this
                                distance = 0
                                paint = Appearance.SourcePoint()
                            }
                            initialVertexesToDraw.size == 1 -> {
                                endVertex = this
                                paint = Appearance.EndPoint()
                            }
                            else -> {
                                isWall = true
                                paint = Appearance.BlockedPoint()
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
            entry.value.find { it.rect.contains(x, y) }?.run {
                vertex = this
            }
        }

        return vertex
    }
}

private const val TAG = "FieldView"