package application.model

import newsstand.components.entity.Customer
import tornadofx.*

class CustomerModel(customer: Customer) : ItemViewModel<Customer>(customer) {
    val arrivedToSystem = bind(Customer::arrivedToSystem)
    val building = bind(Customer::building)
}

