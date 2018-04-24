package application.controller

import application.model.MinibusModel
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.concurrent.Task
import newsstand.NewsstandSimualation
import tornadofx.*

class MyController : Controller() {

    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    val minibuses = FXCollections.observableArrayList<MinibusModel>()

    val s = NewsstandSimualation()

    val simTime = SimpleStringProperty("Init")
    lateinit var asyncThing: Task<Unit>
    fun run() {
        println("x")
        s.setSimSpeed(20.0, 1.0)

        s.onRefreshUI {
            s.state().minibuses.map { MinibusModel(s.currentTime(), it) }.let(minibuses::setAll)
            simTime.set(s.currentTime().toString())
        }

        s.state().minibuses.let { println(it) }

        asyncThing = runAsync { s.start() }
    }
}