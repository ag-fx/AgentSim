package application.view

import application.controller.MyController
import application.model.*
import javafx.beans.property.Property
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventTarget
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.text.Text
import javafx.util.StringConverter
import newsstand.components.entity.Building
import tornadofx.*

class TerminalView : View("Terminals") {
    private val controller: MyController by inject()

    override val root = vbox {
        vgrow = Priority.ALWAYS
        hgrow = Priority.ALWAYS
        hbox {

            Building
                .values()
                .filter { it != Building.AirCarRental }
                .forEach {

                    val queue = when (it) {
                        Building.TerminalOne -> controller.queueT1
                        Building.TerminalTwo -> controller.queueT2
                        Building.AirCarRental -> TODO()
                    }

                    val avgTimeInQueue = when (it) {
                        Building.TerminalOne -> XSim { "Avg time in queue\t${(it.statQueueT1.mean() / 60).format()}" }
                        Building.TerminalTwo -> XSim { "Avg time in queue\t${(it.statQueueT2.mean() / 60).format()}" }
                        else -> TODO()
                    }

                    val length = XSim { "Current queue length\t${queue.size}" }

                    val avgLength = when (it) {
                        Building.TerminalOne -> XSim { "Avg queue length  \t${it.queueT1.mean().format()}" }
                        Building.TerminalTwo -> XSim { "Avg queue length  \t${it.queueT2.mean().format()}" }
                        Building.AirCarRental -> TODO()
                    }
                    val maxLength = when (it) {
                        Building.TerminalOne -> XSim { "Max queue length\t${it.queueT1.max().format()}" }
                        Building.TerminalTwo -> XSim { "Max queue length\t${it.queueT2.max().format()}" }
                        Building.AirCarRental -> TODO()
                    }

                    hbox {
                        borderpane {
                            padding = Insets(20.0)

                            center = vbox {
                                vgrow = Priority.ALWAYS
                                hgrow = Priority.ALWAYS
                                addClass("card")
                                text(it.toString()).addClass("card-title")
                                vgrow = Priority.ALWAYS
                                tableview(queue) {
                                    vgrow = Priority.ALWAYS
                                    hgrow = Priority.ALWAYS
                                    smartResize()
                                    column("Terminal", CustomerModel::building).apply { isSortable = false }
                                    column("Cas vstupu", CustomerModel::arrivedToSystem) {
                                        isSortable = false
                                        converter(DoubleTimeConv())
                                    }
                                }
                            }
                            right = hbox {
                                paddingLeft = 24
                               vbox {
                                    addClass("card")
                                    label("Statistics").addClass("card-title")
                                    text(it.toString())
                                    vbox {
                                        vgrow = Priority.ALWAYS
                                        hgrow = Priority.ALWAYS
                                        text(controller.simStateModel, converter = length).addClass("label")
                                        text(controller.simStateModel, converter = avgLength).addClass("label")
                                        text(controller.simStateModel, converter = maxLength).addClass("label")
                                        text(controller.simStateModel, converter = avgTimeInQueue).addClass("label")
                                    }
                                }
                            }
                        }

                    }
                }

        }
    }
}


fun <T> EventTarget.text(property: Property<T>, converter: StringConverter<T>, op: Text.() -> Unit = {}) = text().apply {
    textProperty().bindBidirectional(property, converter)
    ViewModel.register(textProperty(), property)
    op(this)
}