package application.controller

import application.model.CustomerModel
import application.model.EmployeeModel
import application.model.MinibusModel
import application.model.TerminalModel
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections.observableArrayList
import javafx.concurrent.Task
import newsstand.NewsstandSimualation
import tornadofx.*


class MyController : Controller() {

     val minibuses = observableArrayList<MinibusModel>()
     val terminals = observableArrayList<TerminalModel>()
     val employees = observableArrayList<EmployeeModel>()
     val carRentalQueue = observableArrayList<CustomerModel>()
     val sim = NewsstandSimualation()

    val simTime = SimpleStringProperty("Init")
    lateinit var asyncThing: Task<Unit>

    fun run() {
        sim.setSimSpeed(75.0, 1.0)

        sim.onRefreshUI {
            sim.getState().minibuses.map { MinibusModel(sim.currentTime(), it) }.let(minibuses::setAll)
            sim.getState().terminals.map(::TerminalModel).let(terminals::setAll)
            sim.getState().acrEmployees.map(::EmployeeModel).let(employees::setAll)
            sim.getState().acrQueue.map(::CustomerModel).let(carRentalQueue::setAll)
            simTime.set(sim.currentTime().toString())
        }

        sim.onSimulationWillStart { println("idem") }
        asyncThing = runAsync { sim.start() }
    }
}