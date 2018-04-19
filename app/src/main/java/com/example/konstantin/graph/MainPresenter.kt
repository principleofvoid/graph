package com.example.konstantin.graph

class MainPresenter(private val graph: Graph) {

    private var view: MainActivity? = null

    fun attach(view: MainActivity) {
        this.view = view
    }

    fun detach() {
        view = null
    }

    fun getVertexList(): List<String> = graph.getVertexList()

    fun calculatePath(startVertexName: String, endVertexName: String) {
        val path = graph.calculatePath(startVertexName, endVertexName)
        if (path.isEmpty()) {
            view?.showMessage("Путь между $startVertexName и $endVertexName не найден.")
        } else {
            view?.showPath(path)
        }
    }
}