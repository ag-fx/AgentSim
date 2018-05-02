package newsstand.components

import OSPABA.MessageForm
import OSPABA.Simulation
import newsstand.components.entity.*

class Message : MessageForm {

    var group: Group? = null
    var oneCustomer: Customer? = null
    var minibus: Minibus? = null
    var employee: Employee? = null
    var building: Building? = null


    constructor(customer: Group? = null, sim: Simulation?) : super(sim) {
        this.group = customer
    }

    constructor(original: Message) : super(original) {
        group = original.group
        oneCustomer = original.oneCustomer
        minibus = original.minibus
        employee = original.employee
        building = original.building
    }

    override fun createCopy() = Message(this)

}

fun MessageForm.convert() = this as Message
fun Message.setMinibus(minibus: Minibus) = apply { this.minibus = minibus }
fun Message.setEmployee(employee: Employee) = apply { this.employee = employee }