package application.view

import application.controller.MyController
import application.model.*
import javafx.beans.property.SimpleListProperty
import javafx.geometry.Insets
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
                    text(controller.simStateModel, converter = XSim { "Queue length\t\t${controller.carRentalQueue.size}" })
                    text(controller.simStateModel, converter = XSim { "Avg queue length\t\t${it.queueAcr.mean().format()}" })
                    text(controller.simStateModel, converter = XSim { "Max queue length\t\t${it.queueAcr.max().format()}" })

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
                    smartResize()
                    column("Id", EmployeeModel::id).apply { isSortable = false }
                    column("Obsluhuje", EmployeeModel::isBusy).apply { isSortable = false }
                    column("Zakaznik", EmployeeModel::serving) {
                        isSortable = false
                    }

                }
            }
        }
    }
}