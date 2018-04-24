package newsstand.components

import OSPABA.MessageForm
import OSPABA.Simulation
import newsstand.components.entity.Building
import newsstand.components.entity.Customer
import newsstand.components.entity.Minibus

class Message : MessageForm {

    var customer: Customer? = null
    var minibus: Minibus? = null


    constructor(customer: Customer? = null, sim: Simulation?) : super(sim) {
        this.customer = customer
    }

    constructor(original: Message) : super(original) {
        customer = original.customer
        minibus = original.minibus
    }

    fun setNewCustomer(building: Building) {
        customer = Customer(mySim().currentTime(), building)
    }

    override fun createCopy() = Message(this)

}

fun MessageForm.convert() = this as Message
fun Message.setMinibus(minibus: Minibus) = apply { this.minibus = minibus }