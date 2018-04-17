package newsstand.components.boss

import OSPABA.Agent
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.WrongMessageCode
import abaextensions.toAgent
import abaextensions.withCode
import newsstand.constants.id
import newsstand.constants.mc
import newsstand.constants.mc.customerArrivedToSystem
import newsstand.constants.mc.customerServiceEnded
import newsstand.constants.mc.init
import newsstand.constants.mc.startCustomerService

class BossManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.BossManager, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        init -> message
            .toAgent(id.SurroundingAgent)
            .withCode(init)
            .let { notice(it) }

        customerArrivedToSystem -> message
            .toAgent(id.NewsstandAgent)
            .withCode(startCustomerService)
            .let { request(it) }

        customerServiceEnded -> message
            .toAgent(id.SurroundingAgent)
            .withCode(mc.customerLeftSystem)
            .let { notice(it) }

        else -> throw WrongMessageCode(message)
    }
}
