package com.example.konstantin.graph

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val presenter = MainPresenter(Graph(edges))

    private lateinit var textMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attach(this)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    private fun init() {
        val spinnerStartVertex = findViewById<AppCompatSpinner>(R.id.spinner_start_vertex)
        val spinnerEndVertex = findViewById<AppCompatSpinner>(R.id.spinner_end_vertex)

        val startAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                presenter.getVertexList())
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStartVertex.adapter = startAdapter
        spinnerStartVertex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                presenter.calculatePath(
                        parent?.getItemAtPosition(pos).toString(),
                        spinnerEndVertex.selectedItem.toString())
            }
        }


        val endAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                presenter.getVertexList())
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEndVertex.adapter = endAdapter
        spinnerEndVertex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                presenter.calculatePath(
                        spinnerStartVertex.selectedItem.toString(),
                        parent?.getItemAtPosition(pos).toString())
            }
        }

        textMessage = findViewById(R.id.text_message)
    }

    fun showPath(path: List<String>) {
        val message = getString(R.string.path_message, path.first(), path.last(), path.joinToString())
        textMessage.text = message
    }

    fun showMessage(message: String) {
        textMessage.text = message
    }
}
