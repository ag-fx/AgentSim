package application.view

import application.controller.VariableMinibusController
import application.model.ConfigModel
import javafx.geometry.Insets
import newsstand.Config
import newsstand.components.entity.BusType
import tornadofx.*

class VariableMinibusSettingsView : View("Config") {

    val controller: VariableMinibusController by inject()
    private val confModel = ConfigModel(Config().copy(xtra = 5, employees = 3))
    override val root = vbox {

        addClass("card")
        text("Config").addClass("card-title")
        label("Počet replikácií") { addClass("control-label") }
        textfield {
            intTyper()
            bind(confModel.replicationCount)
            intValidator()
            promptText = "1000"
        }
        label("Min počet minibusov") { addClass("control-label") }
        textfield {
            intTyper()
            bind(confModel.minibuses)
            intValidator()
            promptText = "2"
        }

        label("Max počet minibusov") { addClass("control-label") }
        textfield {
            intTyper()
            bind(confModel.xtra)
            intValidator()
            promptText = "5"
        }

        label("Počet zamestantcov") { addClass("control-label") }
        textfield {
            intTyper()
            bind(confModel.employees)
            intValidator()
            promptText = "3"
        }

        label("Typ minibusov") { addClass("control-label") }
        choicebox(values = BusType.values().toList()) {
            bind(confModel.busType)
            value = BusType.A
        }
        hbox {
            vbox {
                button("Run") {
                    addClass("button-raised")
                    action {
                        confModel.commit() {
                            confModel.item.let { println(it) }
                            controller.run(confModel.item.apply { this.slowDownAfterWarmUp = false })
                        }
                    }
                }
            }
            vbox {
                paddingLeft = 12.0
                progressindicator(controller.simulationProgress) { }
            }
        }

        hbox{
            val insets = Insets(10.0)
            val pl = 6.0
            padding = insets
            button("Stop"){
                padding = insets

                action{
                    controller.stop()
                }
            }
            button("Pause"){
                paddingLeft = pl

                action{
                    controller.pause()
                }
            }
            button("Resume"){
                paddingLeft = pl
                action{
                    controller.resume()
                }
            }
        }

    }
}