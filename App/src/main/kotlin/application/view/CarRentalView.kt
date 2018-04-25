package application.view

import application.controller.MyController
import application.model.CustomerModel
import application.model.EmployeeModel
import application.model.TerminalModel
import javafx.beans.property.SimpleListProperty
import tornadofx.*

class CarRentalView : View("AirCarRental") {
    private val controller: MyController by inject()
    override val root = borderpane {
        left = tableview(controller.carRentalQueue) {
            column("Terminal", CustomerModel::building).apply { isSortable = false }
            column("Cas vstupu", CustomerModel::arrivedToSystem).apply { isSortable = false }
        }
        right = tableview(controller.employees) {
            smartResize()
            column("Id", EmployeeModel::id).apply{ isSortable = false}
            column("Obsluhuje", EmployeeModel::isBusy).apply{ isSortable = false}
            column("Zakaznik", EmployeeModel::serving).apply{ isSortable = false}
        }
    }
}