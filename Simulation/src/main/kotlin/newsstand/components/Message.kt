package newsstand.components

import OSPABA.MessageForm
import OSPABA.Simulation
import newsstand.components.entity.Customer

class Message : MessageForm {

    var customer: Customer? = null

    constructor(customer: Customer? = null, sim: Simulation?) : super(sim) {
        this.customer = customer

    }

    constructor(original: Message) : super(original) {
        customer = original.customer
    }

    fun setNewCustomer() {
        customer = Customer(mySim().currentTime())
    }
    override fun createCopy() = Message(this)
}