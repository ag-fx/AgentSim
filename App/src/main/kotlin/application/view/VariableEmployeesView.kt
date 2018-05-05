package application.view

import application.controller.VariableEmployeesController
import javafx.geometry.Insets
import javafx.scene.chart.NumberAxis
import tornadofx.*

class VariableEmployeesView : View("Závislosť času od počtu zamestnancov") {

    private val controller: VariableEmployeesController by inject()

    override val root = borderpane {
        padding = Insets(20.0)

        left(VariableEmployeesSettingsView::class)
        center = linechart("", NumberAxis(), NumberAxis()) {
            series("Spolocný čas",controller.dataTotal)
            series("Prichadazujuci ",controller.dataIn)
            series("Odchadazujuci",controller.dataOut)
            createSymbols = true
            with(xAxis as NumberAxis) {
                isForceZeroInRange = false
                isAutoRanging= true
            }
            with(yAxis as NumberAxis) {
                isForceZeroInRange = false
                isAutoRanging = true
            }
        }
    }
}