package com.algoholic.algo.sort

import android.graphics.Rect

data class Column(var x: Int = 15, var y: Int = 200)

fun Column.rect(pos: Int): Rect {
    return if (pos == 0) {
        Rect(pos, 0,x, y)
    } else {
        Rect((pos * x), 0,x*pos + x, y)
    }
}