package newsstand.components.rental

import OSPABA.Action
import OSPABA.Agent
import OSPABA.MessageForm
import OSPABA.Simulation
import newsstand.components.convert
import newsstand.components.entity.Employee
import newsstand.constants.id

class AssignEmployeeToCustomerAction(
    mySim: Simulation,
    parent: Agent
) : Action(id.AssignEmployeeToCustomerAction, mySim, parent) {

    override fun execute(msg: MessageForm) = msg
        .convert()
        .let {
            val employee = it.employee!!
            val customer = myAgent().queue.pop()
            it.customer = customer
            employee.isBusy = true
            employee.serving = customer
        }

    override fun myAgent() = super.myAgent() as AirCarRentalAgent
}