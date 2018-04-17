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
import newsstand.components.Message
import newsstand.constants.* // ktlint-disable
import newsstand.constants.mc.customerArrivedToSystem
import newsstand.constants.mc.customerLeftSystem
import newsstand.constants.mc.init

class SurroundingManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.SurroundingManager, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        init -> message
            .toAgentsAssistant(myAgent(), id.CustomerArrivalScheduler)
            .let { startContinualAssistant(it) }

        finish -> message
            .toAgent(id.BossAgent)
            .withCode(customerArrivedToSystem)
            .let { notice(it) }

        customerLeftSystem -> {
            val msg = message as Message
      //      myAgent().timeInSystem.addSample(mySim().currentTime() - msg.customer!!.arrivedToSystem)
        }

        else -> throw WrongMessageCode(message)
    }

    override fun myAgent(): SurroundingAgent = super.myAgent() as SurroundingAgent
}