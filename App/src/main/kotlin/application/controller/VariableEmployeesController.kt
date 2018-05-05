package application.controller

import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.scene.chart.XYChart
import newsstand.Config
import newsstand.NewsstandSimulation
import tornadofx.*

class VariableEmployeesController : MyController() {

    val dataTotal = observableArrayList<XYChart.Data<Number, Number>>()!!
    val dataIn    = observableArrayList<XYChart.Data<Number, Number>>()!!
    val dataOut   = observableArrayList<XYChart.Data<Number, Number>>()!!


    override fun run(config: Config) =
        runAsync {
            (config.employees..config.xtra)
                .map { config.copy(employees = it) }
                .asSequence()
                .forEach {
                    sim = NewsstandSimulation(it)
                    sim.onSimulationDidFinish {
                        tornadofx.runLater { dataTotal.add(sim.config.employees to sim.timeInSystemTotal.mean().toDouble()) }
                    }
                    sim.start()
                }
        } success {
            runAsync {
                (config.employees..config.xtra)
                    .map { config.copy(employees = it) }
                    .asSequence()
                    .forEach {
                        sim = NewsstandSimulation(it)
                        sim.onSimulationDidFinish {
                            tornadofx.runLater { dataOut.add(sim.config.employees to sim.timeInSystemLeaving.mean().toDouble()) }
                        }
                        sim.start()
                    }
            } success {
                runAsync {
                    (config.employees..config.xtra)
                        .map { config.copy(employees = it) }
                        .asSequence()
                        .forEach {
                            sim = NewsstandSimulation(it)
                            sim.onSimulationDidFinish {
                                tornadofx.runLater { dataIn.add(sim.config.employees to sim.timeInSystemIncoming.mean().toDouble()) }
                            }
                            sim.start()
                        }
                }
            }
        }


}

fun <A, B> ObservableList<XYChart.Data<Number, Number>>.add(p: Pair<A, B>) = with(p) { add(XYChart.Data(first as Number, second as Number)) }