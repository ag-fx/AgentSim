package application.view

import application.controller.MyController
import application.model.*
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import tornadofx.* // ktlint-disable

class MainView : View("Hello TornadoFX") {

    private val controller: MyController by inject()

    override val root = borderpane {
        minWidth = 1000.0
        vgrow = Priority.ALWAYS
        hgrow = Priority.ALWAYS
        top = vbox {
            hbox {
                addClass("menu-bar")
                spacing = 10.0
                button("Run")           { action { controller.run() } }
                button("Full speed") { action { controller.fullSpeed() } }
                button("Slow speed") { action { controller.setSimSpeed() } }
                button("PAUSE") { action { controller.pause() } }
                button("RESUME") { action { controller.resume() } }
                textfield(controller.simTime,converter = NumberTimeConv())
            }
        }
        center = tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            tab(MinibusesView::class)
            tab(TerminalView::class)
            tab(CarRentalView::class)
        }
        bottom = vbox {
            textfield(controller.intervalProperty, converter = DoubleConv { "Interval : " + it.format() })
            slider(min = 1, max = 60*60) {
                isShowTickLabels = true
                valueProperty().bindBidirectional(controller.intervalProperty)
                setOnMouseReleased { controller.setSimSpeed() }

            }
            textfield(controller.durationProperty, converter = DoubleConv { "Duration : " + it.format() })
            slider(min = 0.1, max = 3) {
                isShowTickLabels = true
                valueProperty().bindBidirectional(controller.durationProperty)
                setOnMouseReleased { controller.setSimSpeed() }
            }
        }
    }.also { primaryStage.setOnCloseRequest { System.exit(0) } }


}




