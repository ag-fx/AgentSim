package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.Simulation
import abaextensions.addOwnMessages
import newsstand.components.entity.TerminalOne
import newsstand.components.entity.TerminalTwo
import newsstand.constants.id
import newsstand.constants.mc

class TerminalAgent(
    mySim: Simulation,
    parent: Agent
) : Agent(id.TerminalAgentID, mySim, parent) {

    val terminalOne = TerminalOne(mySim)
    val terminalTwo = TerminalTwo(mySim)

    init {
        TerminalManager(mySim, this)
        EnterBusTerminalOneScheduler(mySim, this, terminalOne)
        EnterBusTerminalTwoScheduler(mySim, this, terminalTwo)

        EnterBusT1(mySim, this, terminalOne)
        EnterBusT2(mySim, this, terminalTwo)

        addOwnMessages(
            mc.customerArrivalTerminalOne,
            mc.customerArrivalTerminalTwo,
            mc.terminalOneMinibusArrival,
            mc.customerEnteredBus
        )
    }

}