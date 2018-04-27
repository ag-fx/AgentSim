package newsstand.components.rental

import OSPABA.Agent
import OSPABA.*
import newsstand.components.convert
import newsstand.constants.id

class MoveCustomerToQueueAction(
    mySim: Simulation,
    parent: Agent
) : Action(id.AirCarRentalMoveCustomerToQueueAction, mySim, parent) {

    override fun execute(msg: MessageForm) = msg
        .convert()
        .let {
            myAgent().queue.push(it.customer)
            it.customer = null
        }

    override fun myAgent() = super.myAgent() as AirCarRentalAgent
}


