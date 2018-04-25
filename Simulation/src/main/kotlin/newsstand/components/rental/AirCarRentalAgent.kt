package newsstand.components.rental

import OSPABA.Agent
import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.WStat
import abaextensions.addOwnMessages
import newsstand.components.entity.Customer
import newsstand.components.entity.Employee
import newsstand.constants.id
import newsstand.constants.mc

class AirCarRentalAgent(
    mySim: Simulation,
    parent: Agent
) : Agent(id.AirCarRentalAgentID, mySim, parent) {

    val queue = SimQueue<Customer>(WStat(mySim))
    val employees = List(5) { Employee(it) }

    init {
        AirCarRentalManager(mySim, this)
        GetOffBusScheduler(mySim, this)
        CustomerServiceScheduler(mySim, this)
        addOwnMessages(
            mc.airCarRentalMinibusArrival,
            mc.customerGotOffBus,
            mc.customerServed
        )
    }

}


