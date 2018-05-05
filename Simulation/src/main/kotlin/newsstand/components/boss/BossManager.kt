package newsstand.components.boss

import OSPABA.*
import OSPABA.IdList.finish
import abaextensions.WrongMessageCode
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.Config
import newsstand.components.convert
import newsstand.constants.id
import newsstand.constants.mc
import newsstand.constants.mc.init

class BossManager(
    mySim: Simulation,
    myAgent: Agent,
    val config: Config
) : Manager(id.BossManager, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        finish -> when (message.sender()) {

            is AfterWarmUpScheduler -> {
                if (config.slowDownAfterWarmUp) mySim().setSimSpeed(1.0, 1.0)
                listOf(id.TerminalAgentID, id.AirCarRentalAgentID,id.MinibusAgentID)
                    .forEach {
                        message
                            .createCopy()
                            .withCode(mc.clearLengthStat)
                            .toAgent(it)
                            .let { notice(it) }
                    }
            }
            is TestSchedler -> {

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

            else -> {
            }
        }

        init -> {
            message
                .createCopy()
                .convert()
                .toAgentsAssistant(myAgent(), -10) // delay
                .let { startContinualAssistant(it) }

            message
                .createCopy()
                .convert()
                .toAgentsAssistant(myAgent(), -11) // warmupmessage
                .let { startContinualAssistant(it) }
        }


        else -> throw WrongMessageCode(message)
    }
}

