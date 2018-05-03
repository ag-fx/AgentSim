package application.view

import application.controller.MyController
import application.model.*
import javafx.beans.property.SimpleListProperty
import javafx.geometry.Insets
import javafx.scene.layout.Priority
import tornadofx.*

class CarRentalView : View("AirCarRental") {
    private val controller: MyController by inject()
    override val root = borderpane {
        padding = Insets(20.0)

        center = borderpane {
            center = vbox {
                addClass("card")
                text("AirCarRental").addClass("card-title")

                tableview(controller.carRentalQueue) {
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
                  //label(controller.simStateModel, converter = XSim { "Cur queue length\t\t${controller.carRentalQueue.size}" })
//                    label(controller.simStateModel, converter = XSim { "Avg queue length\t\t${it.queueAcr.mean().format()}" })
//                    label(controller.simStateModel, converter = XSim { "Max queue length\t${it.queueAcr.max().format()}" })

                }
            }
        }

        right = hbox {
            paddingLeft = 24

            vbox {
                addClass("card")
                text("Employees").addClass("card-title")
                tableview(controller.employees) {
                    minWidth = 300.0
                    vgrow = Priority.ALWAYS
                    hgrow = Priority.ALWAYS
                    smartResize()
                    column("Id", EmployeeModel::id).apply { isSortable = false }
                    column("Obsluhuje", EmployeeModel::isBusy).apply { isSortable = false }
                    column("Zakaznik", EmployeeModel::serving) {
                        isSortable = false
                        converter(CustomerConverter())
                    }
                }
            }
            hbox {
                paddingLeft = 24
                vbox {
                    addClass("card")
                    text("Customers to T3").addClass("card-title")
                    tableview(controller.carRentalQueueToT3) {
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
            }
        }
    }
}