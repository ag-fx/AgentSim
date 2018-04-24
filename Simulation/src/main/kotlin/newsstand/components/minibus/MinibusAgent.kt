package newsstand.components.minibus

import OSPABA.Agent
import OSPABA.Simulation
import abaextensions.addOwnMessages
import newsstand.components.entity.Building
import newsstand.components.entity.Minibus
import newsstand.constants.id
import newsstand.constants.mc.init

class MinibusAgent(
    mySim: Simulation,
    parent: Agent
) : Agent(id.MinibusAgentID, mySim, parent) {

    val minibuses = List(5) { Minibus(it, Building.AirCarRental, Building.TerminalOne, .0) }

    init {
        MinibusManager(mySim, this)
        addOwnMessages(init)
    }

}



