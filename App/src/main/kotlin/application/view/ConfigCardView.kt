package application.view

import application.controller.MyController
import application.model.ConfigModel
import newsstand.Config
import newsstand.components.entity.BusType
import tornadofx.*

class ConfigCardView : View("Config") {
    val controller: MyController by inject()
    private val confModel = ConfigModel(Config())
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
        label("Počet minibusov") { addClass("control-label") }
        textfield {
            intTyper()
            bind(confModel.minibuses)
            intValidator()
            promptText = "3"
        }

        label("Počet zamestnacov") { addClass("control-label") }
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


}