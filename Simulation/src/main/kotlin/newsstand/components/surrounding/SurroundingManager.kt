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
            is TerminalOneCustomerArrivalScheduler -> message.notifyBossTerminalArrival(mc.customerArrivalTerminalOne)
            is TerminalTwoCustomerArrivalScheduler -> message.notifyBossTerminalArrival(mc.customerArrivalTerminalTwo)
            else -> throw IllegalStateException("Wrong finish sender")
        }

        else -> throw WrongMessageCode(message)
    }

    override fun myAgent() = super.myAgent() as SurroundingAgent

    private fun MessageForm.notifyBossTerminalArrival(id: Int) = this
        .createCopy()
        .toAgent(newsstand.constants.id.BossAgent)
        .withCode(id)
        .let { notice(it) }

}