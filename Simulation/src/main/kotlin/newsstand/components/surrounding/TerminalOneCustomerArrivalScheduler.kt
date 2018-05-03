package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import newsstand.components.Message
import newsstand.components.entity.Building
import newsstand.constants.const
import newsstand.constants.id
import com.sun.javaws.Globals
import kotlin.math.roundToInt


class TerminalOneCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.TerminalOne, id.TerminalOneCustomerArrivalScheduler) {

    override val means = listOf(4, 8, 12, 15, 18, 14, 13, 10, 4, 6, 10, 14, 16, 15, 7, 3, 4, 2).map(Int::toDouble)

}