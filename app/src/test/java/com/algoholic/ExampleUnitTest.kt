package com.algoholic

import com.algoholic.graph.Edge
import com.algoholic.graph.Graph
import com.algoholic.graph.Node
import org.junit.Test

import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val graph = Graph()

        val a = Node("A", mutableListOf())
        val b = Node("B", mutableListOf())
        val c = Node("C", mutableListOf())
        val d = Node("D", mutableListOf())
        val e = Node("E", mutableListOf())

        graph.addNode(a)

        //From A to B and C
        a.edges.add(Edge(a, b, 1))
        a.edges.add(Edge(a, c, 2))

        //From B to D
        b.edges.add(Edge(b, d, 3))

        //From C to D
        c.edges.add(Edge(c, d, 4))

        //From D to E
        d.edges.add(Edge(d, e, 5))

        //From D to E
        d.edges.add(Edge(d, e, 6))

        b.edges.add(Edge(b, e, 7))

        c.edges.add(Edge(c, e, 8))

        graph.print()
    }

    @Test
    fun test() {
//        val cell1 = Cell(2,4,3, mock())
//        val cell2 = Cell(2,5,3)
//        val cell3 = Cell(2,6,3)
//        val cell4 = Cell(2,7,3)
//        val cell5 = Cell(2,8,3)


        val edges: LinkedList<Int> = LinkedList(listOf(1, 2, 3, 4))

            var iterator = edges.listIterator()

            if (iterator.hasNext()) {
                iterator.next()
            }

           iterator = edges.listIterator(edges.indexOf(3))
            if (iterator.hasPrevious()) {
                iterator.previous()
            }
    }
}