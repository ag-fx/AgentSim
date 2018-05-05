package application.view

import application.controller.VariableMinibusController
import javafx.geometry.Insets
import javafx.scene.chart.NumberAxis
import tornadofx.*

class VariableMinibusView : View("Závislosť času od počtu minibusov") {

    private val controller: VariableMinibusController by inject()

    override val root = borderpane {
        padding = Insets(20.0)

        left(VariableMinibusSettingsView::class)
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