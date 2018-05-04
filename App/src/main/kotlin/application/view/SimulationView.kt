package application.view

import application.controller.MyController
import application.model.DoubleConv
import application.model.ResultModel
import application.model.format
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import newsstand.components.entity.BusType
import tornadofx.*

class Config : View("Config") {
    private val controller: MyController by inject()

    override val root = borderpane {
        padding = Insets(20.0)

        left = vbox {
            addClass("card")
            text("Config").addClass("card-title")
            label("Počet replikácií") { addClass("control-label") }
            textfield { promptText = "1000" }
            label("Počet minibusov") { addClass("control-label") }
            textfield { promptText = "3" }
            label("Typ") { addClass("control-label") }
            choicebox(values = BusType.values().toList()) { value = BusType.A }
            label("Počet zamestnacov") { addClass("control-label") }
            textfield { promptText = "3" }
            label("Spomaliť po zahriatí") { addClass("control-label") }
            checkbox("Ano") { }
            hbox {

                vbox {
                    button("Run") {
                        addClass("button-raised")
                        action { controller.run() }
                    }
                }
                vbox {
                    paddingLeft = 12.0
                    progressindicator(controller.simulationProgress) { }
                }
            }

        }

        center = vbox {
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            paddingLeft = 12.0
            vbox {
                hgrow = Priority.ALWAYS
                vgrow = Priority.ALWAYS
                addClass("card")
                text("Stats").addClass("card-title")
                tableview(controller.stats) {
                    vgrow = Priority.ALWAYS
                    columnResizePolicy = SmartResize.POLICY
                    column("Štatistika", ResultModel::name)
                    column("Priemer", ResultModel::mean)
                    column("90% interval spoľahlivosti", ResultModel::confidence90)
                    column("95% interval spoľahlivosti", ResultModel::confidence95)
                }
            }
        }

    }
}