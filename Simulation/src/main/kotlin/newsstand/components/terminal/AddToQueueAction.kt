package newsstand.components.terminal

import OSPABA.*
import newsstand.components.convert
import newsstand.constants.id
import newsstand.constants.mc

class AddToTerminalQueueAction(
    mySim: Simulation,
    parent: Agent
) : Action(id.AssignEmployeeToCustomerAction, mySim, parent) {

    override fun execute(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .let {
            when(it.code()){
                mc.customerArrivalTerminalOne -> myAgent().terminalOne.queue.push(it.customer!!)
                mc.customerArrivalTerminalTwo -> myAgent().terminalTwo.queue.push(it.customer!!)
            }
        }

    override fun myAgent() = super.myAgent() as TerminalAgent
}
