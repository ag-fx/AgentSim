package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import OSPStat.Stat
import abaextensions.addOwnMessages
import newsstand.constants.id
import newsstand.constants.mc

class SurroundingAgent(
    mySim: Simulation,
    parent: Agent
) : Agent(id.SurroundingAgent, mySim, parent) {

    val timeInSystem = Stat()

    init {
        SurroundingManager(mySim, this)
        CustomerArrivalScheduler(mySim, this)
        addOwnMessages(mc.init, mc.customerLeftSystem, mc.newCustomer)
    }
}