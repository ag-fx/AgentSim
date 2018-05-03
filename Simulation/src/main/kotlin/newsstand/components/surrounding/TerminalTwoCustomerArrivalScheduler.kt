package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import newsstand.components.Message
import newsstand.components.entity.Building
import newsstand.constants.const
import newsstand.constants.id

class TerminalTwoCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.TerminalTwo, id.TerminalTwoCustomerArrivalScheduler) {

    override val means = listOf(3, 6, 9, 15, 17, 19, 14, 6, 3, 4, 21, 14, 19, 12, 5, 2, 3, 3).map(Int::toDouble)

}
