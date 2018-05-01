package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.NewsstandSimulation
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.constants.id
import newsstand.constants.mc.newCustomer

class TerminalTwoCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.TerminalTwo, id.TerminalTwoCustomerArrivalScheduler) {

    override fun customerArrived(msg: Message) {
        msg.customer = createOneCustomer().value
    }

    override fun timeBetweenArrivals(): Double {
        val currentTimeIndex = getIntervalIndex(mySim().currentTime())
        var generated = generators[currentTimeIndex].sample() + mySim().currentTime()
        val nextArrivalTimeIndex = getIntervalIndex(mySim().currentTime() + generated)
        var indexBoundary = currentTimeIndex * interval + interval + startTime
        val current = mySim().currentTime().toTime()
        while (generated > indexBoundary) {
            indexBoundary += interval
            var s = generated - (indexBoundary - interval)
            val alfa = getIntervalIndex(indexBoundary - interval)
            val alfaPlusJedna = getIntervalIndex(indexBoundary)
            if (alfaPlusJedna == 18) {
                return 20.5 * 3600.0
                //  break
            }
            val a = means[alfa]
            val b = means[alfaPlusJedna]
            var scale = s * means[alfa] / means[alfaPlusJedna]
            generated = scale + (indexBoundary - interval)
            println()
        }
        val gen = (generated).toTime()
        if (generated - mySim().currentTime() < 0)
            return 0.0
        else
            return generated - mySim().currentTime()
    }

    private val rnd = ExponentialRNG(3600.0 / 19.0)

    override val means = listOf(3, 6, 9, 15, 17, 19, 14, 6, 3, 4, 21, 14, 19, 12, 5, 2, 3, 3).map(Int::toDouble)

}
