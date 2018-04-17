package application.view

import application.controller.MyController
import tornadofx.* // ktlint-disable

class MainView : View("Hello TornadoFX") {

    private val myController: MyController by inject()

    override val root = vbox {
        label(myController.textProperty)
        myController.run()
        textfield { text = "live ui :)" }
    }
}