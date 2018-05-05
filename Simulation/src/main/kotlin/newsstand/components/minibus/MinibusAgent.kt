package newsstand.components.minibus

import OSPABA.Agent
import OSPABA.Simulation
import abaextensions.addOwnMessages
import newsstand.Config
import newsstand.components.entity.Building
import newsstand.components.entity.Minibus
import newsstand.constants.Clearable
import newsstand.constants.id
import newsstand.constants.mc
import newsstand.constants.mc.init

class MinibusAgent(
    mySim: Simulation,
    parent: Agent,
    conf: Config
) : Agent(id.MinibusAgentID, mySim, parent), Clearable {

    val minibuses = List(conf.minibuses) { Minibus(it, Building.AirCarRental, Building.TerminalOne, .0,type = conf.busType) }

    init {

        MinibusManager(mySim, this)
        MinibusMovementStart(mySim, this)
        MinibusMovement(mySim, this)
        ExitFromMinibusScheduler(mySim, this)
        EnterToMinibusMinibusScheduler(mySim, this)

        addOwnMessages(
            init,
            mc.minibusGoTo,
            mc.minibusArrivedToDestination,
            mc.getCustomerFromBusRequest,
            mc.customerExitedMinibus,
            mc.customerEnteredMinibus,
            mc.enterMinibusRequest,
            mc.getCustomerFromBusResponse,
            mc.clearLengthStat

        )
    }

    override fun clear() {
        minibuses.forEach(Minibus::clear)
    }

}