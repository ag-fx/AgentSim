package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.Customer
import newsstand.components.entity.Terminal
import newsstand.constants.id
import newsstand.constants.mc


class TerminalManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.TerminalManagerID, mySim, myAgent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        mc.customerArrivalTerminalOne,
        mc.customerArrivalTerminalTwo -> addCustomerToQueue(msg)

        mc.terminalOneMinibusArrival -> handleBusOnTerminal(myAgent().terminalOne, msg)

        mc.terminalTwoMinibusArrival -> handleBusOnTerminal(myAgent().terminalTwo, msg)

        mc.enterMinibusResponse -> handleBusOnTerminal(msg)

        else -> {
        }
    }

    private fun handleBusOnTerminal(msg:MessageForm) = msg.createCopy().convert().let {
        when (it.building!!) {
            Building.TerminalOne -> handleBusOnTerminal(myAgent().terminalOne, msg)
            Building.TerminalTwo -> handleBusOnTerminal(myAgent().terminalTwo, msg)
            else -> throw IllegalStateException()
        }
    }

    private fun handleBusOnTerminal(terminal: Terminal, msg: MessageForm) {
        if (terminal.queue.isNotEmpty() && msg.convert().minibus!!.isNotFull()) {
            val customer = terminal.queue.pop()
            requestLoading(msg, customer, terminal)
        } else{
            msg.createCopy().toAgent(id.MinibusAgentID).withCode(mc.minibusGoTo).let { notice(it) }
        }

    }

    private fun requestLoading(msg: MessageForm, customer: Customer, terminal: Terminal) = msg
        .createCopy()
        .convert()
        .apply {
            this.customer = customer
            this.building = terminal.building
        }
        .withCode(mc.enterMinibusRequest)
        .toAgent(id.MinibusAgentID)
        .let { request(it) }


    private fun addCustomerToQueue(msg: MessageForm) = msg
        .createCopy()
        .toAgentsAssistant(myAgent(), id.AssignEmployeeToCustomerAction)
        .let { execute(it) }

    override fun myAgent() = super.myAgent() as TerminalAgent
}
