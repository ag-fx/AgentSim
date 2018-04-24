package application.view

import application.controller.MyController
import tornadofx.* // ktlint-disable

class MainView : View("Hello TornadoFX") {

    private val controller: MyController by inject()

    override val root = vbox {
        hbox {
            button("Run") { action { controller.run() } }
            textfield(controller.simTime)
        }
        tabpane {
            tab(MinibusesView::class)
        }
    }

}


