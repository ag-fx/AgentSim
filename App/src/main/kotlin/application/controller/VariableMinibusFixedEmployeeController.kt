package application.controller

import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import newsstand.Config
import newsstand.NewsstandSimulation
import tornadofx.*

class VariableMinibusController : MyController(){

    val dataTotal = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()!!
    val dataIn    = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()!!
    val dataOut   = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()!!


    override fun run(config: Config) =
        runAsync {
            (config.minibuses..config.xtra)
                .map { config.copy(minibuses = it) }
                .asSequence()
                .forEach {
                    sim = NewsstandSimulation(it)
                    sim.onSimulationDidFinish {
                        tornadofx.runLater { dataTotal.add(sim.config.minibuses to sim.timeInSystemTotal.mean().toDouble()) }
                    }
                    sim.start()
                }
        } success {
            runAsync {
                (config.minibuses..config.xtra)
                    .map { config.copy(minibuses = it) }
                    .asSequence()
                    .forEach {
                        sim = NewsstandSimulation(it)
                        sim.onSimulationDidFinish {
                            tornadofx.runLater { dataOut.add(sim.config.minibuses to sim.timeInSystemLeaving.mean().toDouble()) }
                        }
                        sim.start()
                    }
            } success {
                runAsync {
                    (config.minibuses..config.xtra)
                        .map { config.copy(minibuses = it) }
                        .asSequence()
                        .forEach {
                            sim = NewsstandSimulation(it)
                            sim.onSimulationDidFinish {
                                tornadofx.runLater { dataIn.add(sim.config.minibuses to sim.timeInSystemIncoming.mean().toDouble()) }
                            }
                            sim.start()
                        }
                }
            }
        }

}