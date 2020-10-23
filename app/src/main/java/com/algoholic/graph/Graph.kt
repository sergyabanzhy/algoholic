package com.algoholic.graph

data class Graph( val nodes: MutableList<Node> = mutableListOf()) {
    fun addNode(node: Node) {
        nodes.add(node)
    }

    fun removeNode(node: Node) {
        nodes.remove(node)
    }

    fun print() {
        nodes.forEach {
           printNode(it)
        }
    }

    private fun printNode(node: Node) {
        println("Nodes for - ${node.name}:")
        node.edges.forEach { edge ->
            println("start - ${edge.start.name}, end - ${edge.end.name}, value - ${edge.value}")
            printNode(edge.end)
        }
    }
}