package com.example.konstantin.graph

import java.util.TreeSet

data class Edge(val vertexName1: String, val vertexName2: String, val distance: Int)

private class Vertex(val name: String): Comparable<Vertex> {

    //Минимальное расстояние от стартовой точки до этой вершины
    var distance = Int.MAX_VALUE
    //Расстояния до соседних вершин
    val neighbours = HashMap<Vertex, Int>()
    //Предыдущая вершина на пути к стартовой точке
    var previous: Vertex? = null

    override fun compareTo(other: Vertex): Int {
        return if (distance == other.distance) {
            name.compareTo(other.name)
        } else {
            distance.compareTo(other.distance)
        }
    }
}

//Graph преобразует массив ребер в набор вершин с указанием соседей
//и реализует алгоритм поиска путей на этом наборе.
class Graph(edges: Array<Edge>) {

    private val graph = HashMap<String, Vertex>(edges.size)

    init {
        for (edge in edges) {
            if (!graph.containsKey(edge.vertexName1)) {
                graph[edge.vertexName1] = Vertex(edge.vertexName1)
            }
            if (!graph.containsKey(edge.vertexName2)) {
                graph[edge.vertexName2] = Vertex(edge.vertexName2)
            }

            graph[edge.vertexName1]!!.neighbours[graph[edge.vertexName2]!!] = edge.distance
            graph[edge.vertexName2]!!.neighbours[graph[edge.vertexName1]!!] = edge.distance
        }
    }

    fun getVertexList(): List<String> = graph.keys.toList().sorted()

    fun calculatePath(startVertexName: String, endVertexName: String): List<String> {
        //Проверка, что заданные вершины реально присутствуют в графе
        val startVertex = graph[startVertexName] ?: return emptyList()
        val endVertex = graph[endVertexName] ?: return emptyList()

        //Путь нулевой длины
        if (startVertexName == endVertexName) {
            return arrayListOf(startVertexName)
        }

        //Рассчитать кратчайшие пути от старта до всех вершин
        dijkstra(startVertex)

        //Построить путь от финиша до старта
        val path = ArrayList<String>()
        var vertex = endVertex

        path.add(vertex.name)
        while ((vertex.previous != null) && (vertex.previous != vertex)) {
            path.add(vertex.previous!!.name)
            vertex = vertex.previous!!
        }

        //Не найден путь между вершинами
        if (vertex.previous == null) {
            return emptyList()
        }

        return if (vertex.previous == vertex) {
            //Путь найден
            path.reversed()
        } else {
            //Произошло что-то непредвиденное
            emptyList()
        }
    }

    private fun dijkstra(startVertex: Vertex) {
        startVertex.distance = 0
        startVertex.previous = startVertex

        val vertexSet = TreeSet<Vertex>()
        vertexSet.addAll(graph.values)

        while (vertexSet.isNotEmpty()) {
            val closestVertex = vertexSet.pollFirst()

            //Если остались только вершины с "бесконечным" расстоянием,
            //алгоритм можно прерывать - эти вершины недостижимы от точки старта
            if (closestVertex.distance == Int.MAX_VALUE) {
                break
            }

            for (neighbour in closestVertex.neighbours) {
                val vertex = neighbour.key

                val distanceToStart = closestVertex.distance + neighbour.value
                if (distanceToStart < vertex.distance) {
                    vertexSet.remove(vertex)
                    vertex.distance = distanceToStart
                    vertex.previous = closestVertex
                    vertexSet.add(vertex)
                }
            }
        }
    }
}

val edges = arrayOf(
        Edge(vertexName1 = "1", vertexName2 = "2", distance = 7),
        Edge(vertexName1 = "1", vertexName2 = "3", distance = 9),
        Edge(vertexName1 = "1", vertexName2 = "6", distance = 14),
        Edge(vertexName1 = "2", vertexName2 = "3", distance = 10),
        Edge(vertexName1 = "2", vertexName2 = "4", distance = 15),
        Edge(vertexName1 = "3", vertexName2 = "4", distance = 11),
        Edge(vertexName1 = "3", vertexName2 = "6", distance = 2),
        Edge(vertexName1 = "4", vertexName2 = "5", distance = 6),
        Edge(vertexName1 = "5", vertexName2 = "6", distance = 9))