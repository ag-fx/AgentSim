package application.view

import application.controller.MyController
import application.model.*
import javafx.beans.property.SimpleListProperty
import tornadofx.*

class CarRentalView : View("AirCarRental") {
    private val controller: MyController by inject()
    override val root =
        hbox {
            hbox {
                tableview(controller.carRentalQueue) {
                    smartResize()
                    column("Terminal", CustomerModel::building).apply { isSortable = false }
                    column("Cas vstupu", CustomerModel::arrivedToSystem).apply { isSortable = false }
                }

                vbox {
                    text("AirCarRental")
                    text(controller.simStateModel, converter = XSim { "Queue length\t\t${controller.carRentalQueue.size}" })
                    text(controller.simStateModel, converter = XSim { "Avg queue length\t${it.queueAcr.mean().format()}" })
                    text(controller.simStateModel, converter = XSim { "Max queue length\t${it.queueAcr.max().format()}" })

                }
            }
            spacer()
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