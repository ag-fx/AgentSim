package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.IdList.finish
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.WrongMessageCode
import abaextensions.toAgentsAssistant
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.Terminal
import newsstand.constants.id
import newsstand.constants.mc


class TerminalManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.TerminalManagerID, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        mc.customerArrivalTerminalOne,
        mc.customerArrivalTerminalTwo -> message.convert().addCustomerToQueue()

        mc.terminalOneMinibusArrival -> terminalArrival(myAgent().terminalOne, message)
        mc.terminalTwoMinibusArrival -> terminalArrival(myAgent().terminalTwo, message)

        finish -> when(message.sender()){
            is GetOnBusTerminalOneScheduler -> terminalArrival(myAgent().terminalOne, message)
            is GetOnBusTerminalTwoScheduler -> terminalArrival(myAgent().terminalTwo, message)
            else -> throw IllegalStateException()
        }

        else -> throw WrongMessageCode(message)
    }

    private fun terminalArrival(terminal: Terminal, msg: MessageForm) {
        val msg = msg.createCopy()
        val minibus = msg.convert().minibus!!
        if (terminal.queue.isNotEmpty() || minibus.isNotFull())
            startLoading(terminal, msg)
        else
            goToNextStop(terminal, msg)
    }

    private fun startLoading(terminal: Terminal, msg: MessageForm) = msg
        .createCopy()
        .toAgentsAssistant(myAgent(), getOnBusAssistantID(terminal))
        .let { startContinualAssistant(it) }

    private fun getOnBusAssistantID(terminal: Terminal) = when (terminal.building) {
        Building.TerminalOne  -> id.GetOnBusTerminalOne
        Building.TerminalTwo  -> TODO()
        Building.AirCarRental -> TODO()
    }

    private fun goToNextStop(terminal: Terminal,msg: MessageForm){

    }
    private fun Message.addCustomerToQueue() = when (this.code()) {
        mc.customerArrivalTerminalOne -> myAgent().terminalOne.queue.add(customer)
        mc.customerArrivalTerminalTwo -> myAgent().terminalTwo.queue.add(customer)
        else -> throw WrongMessageCode(this)
    }.let { Unit }

    override fun myAgent() = super.myAgent() as TerminalAgent
}
