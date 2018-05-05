package application.view

import application.controller.MyController
import application.model.ConfigModel
import application.model.ResultModel
import javafx.geometry.Insets
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import newsstand.Config
import newsstand.components.entity.BusType
import tornadofx.*

class ConfigView : View("Config") {
    private val controller: MyController by inject()
    private val confModel = ConfigModel(Config())


    override val root = borderpane {
        padding = Insets(20.0)


        left = vbox {
            addClass("card")
            text("Config").addClass("card-title")
            label("Počet replikácií") { addClass("control-label") }
            textfield {
                intTyper()
                bind(confModel.replicationCount)
                intValidator()
                promptText = "1000"
            }
            label("Počet minibusov") { addClass("control-label") }
            textfield {
                intTyper()
                bind(confModel.minibuses)
                intValidator()
                promptText = "3"
            }
            label("Typ") { addClass("control-label") }
            choicebox(values = BusType.values().toList()) {
                bind(confModel.busType)
                value = BusType.A
            }
            label("Počet zamestnacov") { addClass("control-label") }
            textfield {
                intTyper()
                bind(confModel.employees)
                intValidator()
                promptText = "3"
            }
            label("Spomaliť po zahriatí") { addClass("control-label") }
            checkbox("Ano").bind(confModel.slowDownAfterWarmUp)
            hbox {

                vbox {
                    button("Run") {
                        addClass("button-raised")
                        action {
                            confModel.commit() {
                                confModel.item.let { println(it) }
                                controller.run(confModel.item)
                            }
                        }
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
                    column("Štatistika", ResultModel::name) { isSortable = false }
                    column("Priemer", ResultModel::mean) { isSortable = false }
                    column("90% interval spoľahlivosti", ResultModel::confidence90) { isSortable = false }
                    column("95% interval spoľahlivosti", ResultModel::confidence95) { isSortable = false }
                }
            }
        }

    }

}

fun TextField.intValidator() = validator {
    if (!it.isInt()) error("Must be int") else null
}

fun TextField.intTyper() = textProperty().onChange { if (it?.matches(Regex("\\d*")) ?: false) text else text = null }


fun String?.isInt(): Boolean {
    try {
        this?.toInt() ?: return false
        return true
    } catch (e: Exception) {
        return false
    }
}