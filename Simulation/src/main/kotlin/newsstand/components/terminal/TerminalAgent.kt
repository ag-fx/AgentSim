package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.Simulation
import abaextensions.addOwnMessages
import newsstand.components.entity.Terminal
import newsstand.components.entity.TerminalOne
import newsstand.components.entity.TerminalTwo
import newsstand.constants.Clearable
import newsstand.constants.id
import newsstand.constants.mc

class TerminalAgent(
    mySim: Simulation,
    parent: Agent
) : Agent(id.TerminalAgentID, mySim, parent), Clearable {

    val terminalOne = TerminalOne(mySim)
    val terminalTwo = TerminalTwo(mySim)

    init {
        TerminalManager(mySim, this)
        AddToTerminalQueueAction(mySim, this)

        addOwnMessages(
            mc.customerArrivalTerminalOne,
            mc.customerArrivalTerminalTwo,
            mc.terminalOneMinibusArrival,
            mc.terminalTwoMinibusArrival,
            mc.terminalThreeMinibusArrival,
            mc.enterMinibusResponse,
            mc.getCustomerFromBusResponse,
            mc.clearLengthStat

        )
    }

    override fun clear() = listOf(terminalOne,terminalTwo).forEach(Terminal::clear)

}