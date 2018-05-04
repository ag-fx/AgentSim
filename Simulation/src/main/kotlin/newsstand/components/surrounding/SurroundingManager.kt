package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.IdList.finish
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.WrongMessageCode
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.constants.id
import newsstand.constants.mc
import newsstand.constants.mc.init

class SurroundingManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.SurroundingManager, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        init -> myAgent()
            .continualAssistants()
            .forEach {
                message
                    .createCopy()
                    .toAgentsAssistant(myAgent(), it.id())
                    .let { startContinualAssistant(it) }
            }

        finish -> when (message.sender()) {
            is TerminalOneCustomerArrivalScheduler  -> {
                myAgent().groupsT1.inc()
                message.notifyCustomerArrival(id.TerminalAgentID, mc.customerArrivalTerminalOne)
            }
            is TerminalTwoCustomerArrivalScheduler  -> {
                myAgent().groupsT2.inc()
                message.notifyCustomerArrival(id.TerminalAgentID, mc.customerArrivalTerminalTwo)
            }
            is AirCarRentalCustomerArrivalScheduler -> {
                myAgent().groupsAcr.inc()
                message.notifyCustomerArrival(id.AirCarRentalAgentID, mc.customerArrivalTerminalAcr)
            }
            else -> throw IllegalStateException("Wrong finish sender")
        }

        mc.customerLeaving -> customerLeaving(message.createCopy())

        else -> throw WrongMessageCode(message)
    }

    private fun MessageForm.notifyCustomerArrival(agentID: Int, messageCode: Int) = this
        .createCopy()
        .toAgent(agentID)
        .withCode(messageCode)
        .let { notice(it) }

    private fun customerLeaving(msg: MessageForm) = msg.createCopy().convert().let {
        val group = it.group!!

        myAgent().timeInSystemTotal.addSample(mySim().currentTime() - group.arrivedToSystem())
        when(group.building()){
            Building.TerminalOne,
            Building.TerminalTwo   -> myAgent().timeInSystemIncoming.addSample(mySim().currentTime() - group.arrivedToSystem())
            Building.TerminalThree -> throw IllegalStateException("Should not happen")
            Building.AirCarRental  -> myAgent().timeInSystemLeaving.addSample(mySim().currentTime() - group.arrivedToSystem())
        }
    }

    override fun myAgent() = super.myAgent() as SurroundingAgent

}