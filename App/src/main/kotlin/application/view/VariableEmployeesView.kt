package application.view

import application.controller.MyController
import application.controller.VariableEmployeesController
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Insets
import javafx.scene.chart.NumberAxis
import tornadofx.*

class VariableEmployeesView : View("Závislosť času od počtu zamestnancov") {

    private val controller: VariableEmployeesController by inject()

    override val root = borderpane {
        padding = Insets(20.0)

        left(VariableEmployeesSettingsView::class)
        center = linechart("", NumberAxis(), NumberAxis()) {
            series("Spolocný čas", controller.dataTotal)
            series("Prichadazujuci ", controller.dataIn)
            series("Odchadazujuci", controller.dataOut)
            createSymbols = true
            with(xAxis as NumberAxis) {
//                isForceZeroInRange = false
                isAutoRanging = true
            }
            with(yAxis as NumberAxis) {
//                isForceZeroInRange = false
                isAutoRanging = true
            }
        }
    }
}

class TimeInSystemGraphView : View("Priemerný čas v simulácii") {

    private val controller: MyController by inject()

    private val timeDataVisible = SimpleBooleanProperty(true)
    private val timeDataInVisible = SimpleBooleanProperty(true)
    private val timeDataOutVisible = SimpleBooleanProperty(true)

    override val root = borderpane {
        padding = Insets(20.0)

        center = linechart("", NumberAxis(), NumberAxis()) {
            series("Spolocný čas",   controller.timeData)
            series("Prichadazujuci ",controller.timeDataIn)
            series("Odchadazujuci",  controller.timeDataOut)

            createSymbols = false
            with(xAxis as NumberAxis) {
              //  isForceZeroInRange = false
                isAutoRanging = true
                title = "Replikácia"
                isLegendVisible = true
            }
            with(yAxis as NumberAxis) {
               // isForceZeroInRange = false
                isAutoRanging = true
                title = "čas v systéme"
                isLegendVisible = true
            }
        }

    }
}