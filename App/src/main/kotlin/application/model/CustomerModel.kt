package application.model

import javafx.util.StringConverter
import newsstand.components.entity.Customer
import tornadofx.*

class CustomerModel(customer: Customer) : ItemViewModel<Customer>(customer) {
    val arrivedToSystem = bind(Customer::arrivedToSystem)
    val building = bind(Customer::building)
}

class CustomerConverter : StringConverter<Customer>() {
    override fun fromString(string: String?) = TODO()

    private val time = MyTime()
    override fun toString(`object`: Customer?) = `object`?.let {
        time.actualTime = it.arrivedToSystem
        return "${it.building} @ $time"
    } ?: "-"


}