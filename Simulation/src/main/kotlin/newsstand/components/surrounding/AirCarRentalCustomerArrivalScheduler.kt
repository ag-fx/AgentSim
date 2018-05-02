package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import newsstand.components.Message
import newsstand.components.entity.Building
import newsstand.constants.id

class AirCarRentalCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.AirCarRental, id.AirCarRentalCustomerArrivalScheduler) {

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

    override val means = listOf(12, 9, 18, 28, 23, 21, 16, 11, 17, 22, 36, 24, 32, 16, 13, 13, 5, 4).map(Int::toDouble)

}