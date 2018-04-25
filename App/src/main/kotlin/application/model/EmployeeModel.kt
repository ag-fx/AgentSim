package application.model

import newsstand.components.entity.Employee
import tornadofx.*

class EmployeeModel(employee: Employee) : ItemViewModel<Employee>(employee) {
    val id = bind(Employee::id)
    val serving = bind(Employee::serving)
    val isBusy = bind(Employee::isBusy)
}