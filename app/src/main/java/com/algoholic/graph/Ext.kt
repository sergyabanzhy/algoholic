package com.algoholic.graph

import android.widget.Toast

fun FieldView.dijcstra() {
    val currentVertex = findCellWithShortestDistance()
    currentVertex?.run {
        //distance is calculated
        currentVertex.disabled = true
        val nbrs = findNeighborsForCell(this)

        nbrs.forEach {
            it.previousVertex = currentVertex
        }

        if (containsEndCell(nbrs)) {
            Toast.makeText(context, "Found", Toast.LENGTH_SHORT).show()
        } else {
            visitedVertices.addAll(nbrs)
            invalidate()
        }
    } ?: run {
        Toast.makeText(context, "Can not be reached", Toast.LENGTH_SHORT).show()
        mainHandler.removeCallbacksAndMessages(null)
    }
}