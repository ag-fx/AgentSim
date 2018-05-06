package application.view

import javafx.scene.control.TabPane
import tornadofx.*


class GraphsTabView : View("Grafy") {
    override val root = borderpane {

        center = tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tab(TimeInSystemGraphView::class)
            tab(VariableEmployeesView::class)
            tab(VariableMinibusView::class)
        }

    }
}

