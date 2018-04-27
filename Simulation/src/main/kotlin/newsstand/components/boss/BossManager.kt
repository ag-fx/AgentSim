package newsstand.components.boss

import OSPABA.Agent
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.WrongMessageCode
import abaextensions.toAgent
import abaextensions.withCode
import newsstand.constants.id
import newsstand.constants.mc.init

class BossManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.BossManager, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        init -> {
            val toSurrounding = message
                .createCopy()
                .toAgent(id.SurroundingAgent)
                .withCode(init)

            val toMinibus = message
                .createCopy()
                .toAgent(id.MinibusAgentID)
                .withCode(init)

            listOf(toSurrounding, toMinibus).forEach {
                notice(it)
            }
        }

        else -> throw WrongMessageCode(message)
    }
}