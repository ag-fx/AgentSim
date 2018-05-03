package newsstand.components.rental

import OSPABA.Agent
import OSPABA.*
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.constants.id

class MoveCustomerToQueueAction(
    mySim: Simulation,
    parent: Agent
) : Action(id.AirCarRentalMoveCustomerToQueueAction, mySim, parent) {

    override fun execute(msg: MessageForm) = msg
        .convert()
        .let {
            when (it.group!!.building()) {
                Building.TerminalOne,
                Building.TerminalTwo -> myAgent().queue.push(it.group!!)
                Building.AirCarRental -> {

                    if (it.group!!.arrivedToSystem() != mySim().currentTime())
                        it.group!!.everyone().toList().forEach(myAgent().queueToTerminal3::push)
                    else {
                        myAgent().totalCustomers += it.group!!.size()
                        myAgent().queue.push(it.group!!)
                    }
                }

            }
        }

    override fun myAgent() = super.myAgent() as AirCarRentalAgent

}