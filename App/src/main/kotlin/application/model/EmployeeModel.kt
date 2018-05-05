package application.model

import javafx.util.StringConverter
import newsstand.components.entity.Customer
import newsstand.components.entity.Employee
import tornadofx.*

class EmployeeModel(employee: Employee) : ItemViewModel<Employee>(employee) {
    val id = bind(Employee::id)
    val serving = bind(Employee::serving)
    val isBusy = bind(Employee::isBusy)
    val occupancy = bind(Employee::occupancy)
    val workTime = bind(Employee::workTime)
}