package application.view

import application.controller.MyController
import javafx.scene.control.TabPane
import tornadofx.* // ktlint-disable

class MainView : View("Hello TornadoFX") {

    private val controller: MyController by inject()

    override val root = borderpane {
        top = hbox {
            button("Run") { action { controller.run() } }
            textfield(controller.simTime)
        }
        center = tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tab(MinibusesView::class)
            tab(TerminalView::class)
            tab(CarRentalView::class)
        }
    }

}


