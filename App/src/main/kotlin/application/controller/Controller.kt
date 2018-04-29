package application.controller

import application.model.*
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections.observableArrayList
import newsstand.NewsstandSimulation
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class MyController : Controller() {

    val intervalProperty = SimpleDoubleProperty(20.0)
    var interval by intervalProperty

    val durationProperty = SimpleDoubleProperty(2.0)
    var duration by durationProperty


    val minibuses = observableArrayList<MinibusModel>()
    val queueT1 = observableArrayList<CustomerModel>()
    val queueT2 = observableArrayList<CustomerModel>()
    val carRentalQueue = observableArrayList<CustomerModel>()
    val employees = observableArrayList<EmployeeModel>()
    val sim = NewsstandSimulation()

    val simTime = SimpleStringProperty("Init")
    val simStateModel = SimpleObjectProperty<SimStateModel>()

    fun run() {
        sim.setSimSpeed(50.0, 2.0)

        sim.onRefreshUI {
            simStateModel.set(SimStateModel(sim.getState()))
            sim.getState().minibuses.map { MinibusModel(sim.currentTime(), it) }.let(minibuses::setAll)
            sim.getState().queueT1.map(::CustomerModel).let(queueT1::setAll)
            sim.getState().queueT2.map(::CustomerModel).let(queueT2::setAll)
            sim.getState().acrEmployees.map(::EmployeeModel).let(employees::setAll)
            sim.getState().queueAcr.map(::CustomerModel).let(carRentalQueue::setAll)
            simTime.set(sim.currentTime().format())
        }
        sim.onSimulationWillStart { println("idem") }
        runAsync { try {sim.start()} catch (e:Exception){} }
    }

    fun setSimSpeed(){
        sim.setSimSpeed(interval, duration)
    }

    fun fullSpeed() {
        sim.setMaxSimSpeed()
    }

    fun pause() = sim.pauseSimulation()

    fun resume() = sim.resumeSimulation()
}

