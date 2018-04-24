package newsstand.components.minibus

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
import newsstand.components.setMinibus
import newsstand.constants.id
import newsstand.constants.id.MinibusMovementStartID
import newsstand.constants.mc

class MinibusManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.MinibusManagerID, mySim, myAgent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        mc.init -> myAgent().minibuses.forEach { minibus ->
            msg
                .createCopy()
                .convert()
                .setMinibus(minibus)
                .toAgentsAssistant(myAgent(), MinibusMovementStartID)
                .let { startContinualAssistant(it) }
        }

        finish -> when (msg.sender()) {
            is MinibusMovementStart -> msg
                .convert()
                .toAgent(id.BossAgent)
                .withCode(mc.terminalOneMinibusArrival)
                .let { notice(it) }

            is MinibusMovement -> TODO()

            else -> throw IllegalStateException()
        }
        else -> throw WrongMessageCode(msg)
    }

    override fun myAgent() = super.myAgent() as MinibusAgent
}