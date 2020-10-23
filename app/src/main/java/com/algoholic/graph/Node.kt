package com.algoholic.graph
//      _______
//      |___A_| <- node
//      |
//      |
//      | <- edge(start - A, end - B)
//      |_______
//      |___B__|

data class Node(val name : String, val edges: MutableList<Edge>)