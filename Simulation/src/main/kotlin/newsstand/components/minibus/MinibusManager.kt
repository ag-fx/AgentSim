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
import newsstand.components.entity.Building
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

        mc.minibusGoTo -> msg
            .createCopy()
            .convert()
            .toAgentsAssistant(myAgent(), id.MinibusMovementID)
            .let { startContinualAssistant(it) }

        finish -> when (msg.sender()) {
            is MinibusMovement,
            is MinibusMovementStart -> msg
                .createCopy()
                .convert()
                .toAgent(id.BossAgent)
                .withCode(getTerminalArrivalCode(msg))
                .let { notice(it) }

            is ExitFromMinibusScheduler -> msg
                .createCopy()
                .withCode(mc.getCustomerFromBusResponse)
                .let { response(it) }

            else -> {
            }
        }

    /** @see ExitFromMinibusScheduler **/
        mc.getCustomerFromBusRequest -> msg
            .createCopy()
            .toAgentsAssistant(myAgent(), id.ExitFromMinibusSchedulerID)
            .let {
                startContinualAssistant(it)
            }

        else -> throw WrongMessageCode(msg)
    }

    private fun getTerminalArrivalCode(msg: MessageForm): Int {
        val msg = msg.convert()
        val source = msg.minibus!!.source
        return when (source) {
            Building.TerminalOne -> mc.terminalOneMinibusArrival
            Building.TerminalTwo -> mc.terminalTwoMinibusArrival
            Building.AirCarRental -> mc.airCarRentalMinibusArrival
        }

    }

    override fun myAgent() = super.myAgent() as MinibusAgent
}