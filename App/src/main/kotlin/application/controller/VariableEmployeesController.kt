package application.controller

import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import newsstand.Config
import newsstand.NewsstandSimulation
import tornadofx.*

class VariableEmployeesController : MyController() {

    val dataTotal = observableArrayList<XYChart.Data<Number, Number>>()!!
    val dataIn = observableArrayList<XYChart.Data<Number, Number>>()!!
    val dataOut = observableArrayList<XYChart.Data<Number, Number>>()!!

    fun simulate(config: Config, f: () -> Unit) = runAsync {
        (config.employees..config.xtra)
            .map { config.copy(employees = it) }
            .asSequence()
            .forEach {
                sim = NewsstandSimulation(it)
                sim.onSimulationDidFinish {
                    f() //tornadofx.runLater { dataTotal.add(sim.config.employees to sim.timeInSystemTotal.mean().toDouble()) }
                }
                sim.start()
            }
    }

    override fun run(config: Config) = simulate(config) {
        tornadofx.runLater { dataTotal.add(sim.config.employees to sim.timeInSystemTotal.mean().toDouble()) }
    } success {
        simulate(config) {
            tornadofx.runLater { dataOut.add(sim.config.employees to sim.timeInSystemLeaving.mean().toDouble()) }
        } success {
            simulate(config) {
                tornadofx.runLater { dataIn.add(sim.config.employees to sim.timeInSystemIncoming.mean().toDouble()) }

            }
        }
    }

}

fun <A, B> ObservableList<XYChart.Data<Number, Number>>.add(p: Pair<A, B>) = with(p) { add(XYChart.Data(first as Number, second as Number)) }