package application.view

import javafx.scene.control.TabPane
import tornadofx.*


class Graphs : View("Grafy") {
    override val root = borderpane {

        center = tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tab(VariableEmployeesView::class)
            tab(VariableMinibusView::class)
        }

    }
}

