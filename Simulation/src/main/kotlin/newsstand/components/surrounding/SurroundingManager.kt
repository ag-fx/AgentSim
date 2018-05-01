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
import newsstand.constants.id
import newsstand.constants.mc
import newsstand.constants.mc.init

class   SurroundingManager(
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
            is TerminalOneCustomerArrivalScheduler -> message.notifyTerminalArrival(mc.customerArrivalTerminalOne)
            is TerminalTwoCustomerArrivalScheduler -> message.notifyTerminalArrival(mc.customerArrivalTerminalTwo)
            else -> throw IllegalStateException("Wrong finish sender")
        }

        mc.customerLeaving -> customerLeaving(message)

        else -> throw WrongMessageCode(message)
    }

    private fun MessageForm.notifyTerminalArrival(id: Int) = this
        .createCopy()
        .toAgent(newsstand.constants.id.TerminalAgentID)
        .withCode(id)
        .let { notice(it) }

    private fun customerLeaving(msg: MessageForm) = msg.createCopy().convert().let {
        val customer = it.customer!!
        myAgent().timeInSystem.addSample(mySim().currentTime() - customer.arrivedToSystem)
    }

    override fun myAgent() = super.myAgent() as SurroundingAgent

}