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

    override fun customerArrived(msg: Message) {
        msg.group = createGroup()
    }
/*
    override fun timeBetweenArrivals(): Double {
        val currentTimeIndex = getIntervalIndex(mySim().currentTime())
        var generated = generators[currentTimeIndex].sample() + mySim().currentTime()
        var indexBoundary = currentTimeIndex * intervalGap + intervalGap + const.WarmUpTime //const.StartTime
        while (generated > indexBoundary) {
            indexBoundary += intervalGap
            val s = generated - (indexBoundary - intervalGap)
            val alfa = getIntervalIndex(indexBoundary - intervalGap)
            val alfaPlusJedna = getIntervalIndex(indexBoundary)
            if (alfaPlusJedna == 18) {
                return 20.5 * 3600.0
            }
            val scale = s * means[alfa] / means[alfaPlusJedna]
            generated = scale + (indexBoundary - intervalGap)
        }
        if (generated - mySim().currentTime() < 0)
            return 0.0
        else
            return generated - mySim().currentTime()
    }
*/
    override val means = listOf(3, 6, 9, 15, 17, 19, 14, 6, 3, 4, 21, 14, 19, 12, 5, 2, 3, 3).map(Int::toDouble)

}
