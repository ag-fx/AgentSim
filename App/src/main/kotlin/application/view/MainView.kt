package application.view

import application.controller.MyController
import application.model.DoubleConv
import application.model.XSim
import application.model.format
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import tornadofx.* // ktlint-disable

class MainView : View("Hello TornadoFX") {

    private val controller: MyController by inject()

    override val root = borderpane {
        top = vbox {
            hbox {
                button("Run") { action { controller.run() } }
                button("Full speed") { action { controller.fullSpeed() } }
                button("Slow speed") { action { controller.setSimSpeed() } }
                button("PAUSE") { action { controller.pause() } }
                button("RESUME") { action { controller.resume() } }

                text(controller.simTime)
            }
            separator()
        }
        center = tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            tab(MinibusesView::class)
            tab(TerminalView::class)
            tab(CarRentalView::class)
        }
        bottom = vbox {
            separator()
            text(controller.intervalProperty, converter = DoubleConv { "Interval : " + it.format() })
            slider(min = 1, max = 300) {
                isShowTickLabels = true
                valueProperty().bindBidirectional(controller.intervalProperty)
                setOnMouseReleased { controller.setSimSpeed() }

            }
            text(controller.durationProperty, converter = DoubleConv { "Duration : " + it.format() })
            slider(min = 1, max = 5) {
                isShowTickLabels = true
                valueProperty().bindBidirectional(controller.durationProperty)
                setOnMouseReleased { controller.setSimSpeed() }
            }
        }
    }.also { primaryStage.setOnCloseRequest { System.exit(0) } }


}




