package newsstand.components.terminal

import OSPABA.*
import newsstand.components.convert
import newsstand.constants.id
import newsstand.constants.mc

class AddToTerminalQueueAction(
    mySim: Simulation,
    parent: Agent
) : Action(id.AddToTerminalQueueAction, mySim, parent) {

    override fun execute(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .let {
            when(it.code()){
                mc.customerArrivalTerminalOne -> myAgent().terminalOne.queue.push(it.oneCustomer!!)
                mc.customerArrivalTerminalTwo -> myAgent().terminalTwo.queue.push(it.oneCustomer!!)
            }
        }

    override fun myAgent() = super.myAgent() as TerminalAgent
}
