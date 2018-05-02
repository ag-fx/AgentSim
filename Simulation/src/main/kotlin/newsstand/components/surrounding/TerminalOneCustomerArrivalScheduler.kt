package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import newsstand.components.Message
import newsstand.components.entity.Building
import newsstand.constants.id

class TerminalOneCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.TerminalOne, id.TerminalOneCustomerArrivalScheduler) {

    override fun customerArrived(msg: Message) {
        msg.group = createGroup()
    }

    override fun timeBetweenArrivals(): Double {
        val currentTimeIndex = getIntervalIndex(mySim().currentTime())
        var generated = generators[currentTimeIndex].sample() + mySim().currentTime()
        var indexBoundary = currentTimeIndex * interval + interval + startTime
        while (generated > indexBoundary) {
            indexBoundary += interval
            val s = generated - (indexBoundary - interval)
            val alfa = getIntervalIndex(indexBoundary - interval)
            val alfaPlusJedna = getIntervalIndex(indexBoundary)
            if (alfaPlusJedna == 18) {
                return 20.5 * 3600.0
            }
            val scale = s * means[alfa] / means[alfaPlusJedna]
            generated = scale + (indexBoundary - interval)
        }
        if (generated - mySim().currentTime() < 0)
            return 0.0
        else
            return generated - mySim().currentTime()
    }

    override val means = listOf(4, 8, 12, 15, 18, 14, 13, 10, 4, 6, 10, 14, 16, 15, 7, 3, 4, 2).map(Int::toDouble)

}
