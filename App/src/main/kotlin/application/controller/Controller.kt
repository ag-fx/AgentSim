package application.controller

import application.model.*
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.chart.XYChart
import newsstand.Config
import newsstand.NewsstandSimulation
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

open class MyController : Controller() {

    val durationProperty = SimpleDoubleProperty(2.0)
    var duration by durationProperty


    val simulationProgress = SimpleDoubleProperty(.0)
    val timeData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()!!

    val minibuses = observableArrayList<MinibusModel>()
    val queueT1 = observableArrayList<CustomerModel>()
    val queueT2 = observableArrayList<CustomerModel>()
    val carRentalQueue = observableArrayList<CustomerModel>()
    val carRentalQueueToT3 = observableArrayList<CustomerModel>()
    val employees = observableArrayList<EmployeeModel>()

    lateinit var sim: NewsstandSimulation
    val simTime = SimpleDoubleProperty(0.0)
    val simStateModel = SimpleObjectProperty<SimStateModel>()

    val stats = observableArrayList<ResultModel>()

    open fun run(config: Config) = runAsync {
        sim = NewsstandSimulation(config)
        sim.onRefreshUI {
            try {
                val x = sim.getState()
                runLater { simStateModel.set(SimStateModel(x)) }
                sim.getState().minibuses.map { MinibusModel(sim.currentTime(), it) }.let(minibuses::setAll)
                sim.getState().queueT1.map(::CustomerModel).let(queueT1::setAll)
                sim.getState().queueT2.map(::CustomerModel).let(queueT2::setAll)
                sim.getState().acrEmployees.map(::EmployeeModel).let(employees::setAll)
                sim.getState().queueAcr.map { it.leader }.map(::CustomerModel).let(carRentalQueue::setAll)
                sim.getState().queueAcrToT3.map(::CustomerModel).let(carRentalQueueToT3::setAll)
                simTime.set(sim.currentTime())
            } catch (e: Throwable) {

            }
        }

        sim.onReplicationDidFinish {
            sim.allResults.map(::ResultModel).let(stats::setAll)
            runLater {
                simulationProgress.set(sim.currentReplication() / sim.replicationCount().toDouble())
                sim.timeInSystemTotal.mean().let { timeData.add(sim.currentReplication() to it.toDouble()) }
            }
        }

        sim.start()
    }

    fun setSimSpeed() {
        sim.setSimSpeed(1.0, duration)
    }

    fun fullSpeed() {
        sim.config.slowDownAfterWarmUp = false
        sim.setMaxSimSpeed()
    }

    fun pause() = sim.pauseSimulation()

    fun resume() = sim.resumeSimulation()
}

