package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import newsstand.components.Message
import newsstand.components.entity.Building
import newsstand.constants.const
import newsstand.constants.id

class AirCarRentalCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.AirCarRental, id.AirCarRentalCustomerArrivalScheduler) {

    override val means = listOf(12, 9, 18, 28, 23, 21, 16, 11, 17, 22, 36, 24, 32, 16, 13, 13, 5, 4).map(Int::toDouble)

}