package newsstand.components.boss

import OSPABA.*
import OSPABA.IdList.finish
import abaextensions.WrongMessageCode
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.convert
import newsstand.constants.id
import newsstand.constants.mc.init

class BossManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.BossManager, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        finish -> {
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

        init -> message
            .createCopy()
            .convert()
            .toAgentsAssistant(myAgent(),-10)
            .let { startContinualAssistant(it) }


        else -> throw WrongMessageCode(message)
    }
}

