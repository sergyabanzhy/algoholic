package com.algoholic.graph

import android.widget.Toast

fun BoardView.dijcstra() {

    //1. vertex with shortest distance to source
    val currentVertex = findCellWithShortestDistance()

    currentVertex?.run {
        //distance is calculated
        currentVertex.calculated = true

        //2. All (in our case max 4) neighbors for currentVertex
        val nbrs = findNeighborsForCellAndUpdateDistance(this)

        //3. Add source vertex to each neighbor
        nbrs.forEach {
            it.previousVertex = currentVertex
        }

        //4. Check endpoint in neighbors list
        if (containsEndCell(nbrs)) {
            Toast.makeText(context, "Found", Toast.LENGTH_SHORT).show()
        } else {

            //5. Add neighbors to visited vertex
            visitedVertices.addAll(nbrs)
            invalidate()
        }
    } ?: run {
        Toast.makeText(context, "Can not be reached", Toast.LENGTH_SHORT).show()
        mainHandler.removeCallbacksAndMessages(null)
    }
}

fun BoardView.astar() {
    //1. vertex with shortest distance to source
    val currentVertex = findCellWithShortestH()

    currentVertex?.run {
        //distance is calculated
        currentVertex.calculated = true

        //2. All (in our case max 4) neighbors for currentVertex
        val nbrs = findNeighborsForCellAndUpdateDistance(this)

        //3. Add source vertex to each neighbor
        nbrs.forEach {
            it.previousVertex = currentVertex
        }

        //4. Check endpoint in neighbors list
        if (containsEndCell(nbrs)) {
            Toast.makeText(context, "Found", Toast.LENGTH_SHORT).show()
        } else {

            //5. Add neighbors to visited vertex
            visitedVertices.addAll(nbrs)
            invalidate()
        }
    } ?: run {
        Toast.makeText(context, "Can not be reached", Toast.LENGTH_SHORT).show()
        mainHandler.removeCallbacksAndMessages(null)
    }
}

fun BoardView.pxFromDp(dp: Float): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}