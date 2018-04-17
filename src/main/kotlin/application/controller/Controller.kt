package application.controller

import application.model.MyModel
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.launch
import tornadofx.Controller
import tornadofx.getValue
import tornadofx.setValue

import coroutines.JavaFx as onUi

class MyController : Controller() {
    private val myModel = MyModel()

    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    fun run() {
        launch(onUi) {
            myModel
                .hello
                .filter { it % 1000 == 0 }
                .consumeEach {
                    text = "$it"
                }
        }
    }
}