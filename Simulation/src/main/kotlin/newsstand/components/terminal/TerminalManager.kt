package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.IdList.finish
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.WrongMessageCode
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.Terminal
import newsstand.components.entity.nextStop
import newsstand.constants.id
import newsstand.constants.mc


class TerminalManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.TerminalManagerID, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        mc.customerArrivalTerminalOne,
        mc.customerArrivalTerminalTwo -> message.convert().addCustomerToQueue()

        mc.terminalOneMinibusArrival  -> terminalArrival(myAgent().terminalOne, message)

        mc.terminalTwoMinibusArrival  -> terminalArrival(myAgent().terminalTwo, message)

        finish -> when (message.sender()) {
            is EnterBusTerminalOneScheduler -> terminalArrival(myAgent().terminalOne, message)
            is EnterBusTerminalTwoScheduler -> terminalArrival(myAgent().terminalTwo, message)
            else -> throw IllegalStateException()
        }

        else -> {}
    }

    private fun terminalArrival(terminal: Terminal, msg: MessageForm) {
        val cpy = msg.createCopy()
        val minibus = cpy.convert().minibus!!
        if (terminal.queue.isNotEmpty() && minibus.isNotFull()){
            cpy.toAgentsAssistant(myAgent(),terminal.enterActionID()).let { execute(it) }
            startLoading(terminal, cpy.createCopy())
        }
        else
            goToNextStop(terminal, cpy.createCopy())
    }

    private fun Terminal.enterActionID() = when(this.building){
        Building.TerminalOne -> id.EnterBusActionT1
        Building.TerminalTwo -> id.EnterBusActionT2
        else  -> throw IllegalStateException()
    }
    private fun startLoading(terminal: Terminal, msg: MessageForm) = msg
        .createCopy()
        .toAgentsAssistant(myAgent(), getOnBusAssistantID(terminal))
        .let { startContinualAssistant(it) }

    private fun getOnBusAssistantID(terminal: Terminal) = when (terminal.building) {
        Building.TerminalOne  -> id.GetOnBusTerminalOne
        Building.TerminalTwo  -> id.GetOnBusTerminalTwo
        Building.AirCarRental -> throw IllegalStateException()
    }

    private fun goToNextStop(terminal: Terminal, msg: MessageForm) = msg
        .createCopy()
        .toAgent(id.MinibusAgentID)
        .withCode(mc.minibusGoTo)
        .convert()
        .let {
            it.minibus!!.source      = terminal.building
            it.minibus!!.destination = terminal.building.nextStop()
            it.minibus!!.leftAt = mySim().currentTime()
            notice(it)
        }

    private fun Message.addCustomerToQueue() = when (this.code()) {
        mc.customerArrivalTerminalOne -> myAgent().terminalOne.queue.add(customer)
        mc.customerArrivalTerminalTwo -> myAgent().terminalTwo.queue.add(customer)
        else -> throw WrongMessageCode(this)
    }.let { Unit }

    override fun myAgent() = super.myAgent() as TerminalAgent
}
