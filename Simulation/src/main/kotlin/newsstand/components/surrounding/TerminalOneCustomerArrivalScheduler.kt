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

    override fun customerArrived(msg: Message) {
        msg.group = createGroup()
    }

/*
    override fun timeBetweenArrivals(): Double {
        val generated: Double
        var interval = 0
        if (mySim().currentTime() <= const.WarmUpTime) {
            interval = 0
            generated = generators[interval].sample()
            return generated
        } else {
            val time = mySim().currentTime() - const.WarmUpTime
            interval = (time/intervalGap).roundToInt()
            if(interval==18) return 0.0
            generated = generators[interval].sample()
            //println()
        }
        var genTime = generated + mySim().currentTime()
        var indexBound = intervalGap * interval + intervalGap + const.WarmUpTime
        while (genTime > indexBound) {
            indexBound += intervalGap
            interval++
            if (interval >= means.size) {
                return +const.WarmUpTime + (4.5 * 3600) - mySim().currentTime()
            }
            val overhang = genTime - (indexBound - intervalGap)
            val scale = overhang * (means[interval - 1] / means[interval])
            genTime = scale + indexBound - intervalGap
        }
        return genTime - mySim().currentTime()

        //val currentTimeIndex = getIntervalIndex(mySim().currentTime())
        //var generated = generators[currentTimeIndex].sample() + mySim().currentTime()
        //var indexBoundary = currentTimeIndex * intervalGap + intervalGap + const.WarmUpTime //const.StartTime
        //while (generated > indexBoundary) {
        //    indexBoundary += intervalGap
        //    val s = generated - (indexBoundary - intervalGap)
        //    val alfa = getIntervalIndex(indexBoundary - intervalGap)
        //    val alfaPlusJedna = getIntervalIndex(indexBoundary)
        //    if (alfaPlusJedna == 18) {
        //        return 20.5 * 3600.0
        //    }
        //    val scale = s * means[alfa] / means[alfaPlusJedna]
        //    generated = scale + (indexBoundary - intervalGap)
        //}
        //if (generated - mySim().currentTime() < 0)
        //    return 0.0
        //else
        //    return generated - mySim().currentTime()
    }
*/
    override val means = listOf(4, 8, 12, 15, 18, 14, 13, 10, 4, 6, 10, 14, 16, 15, 7, 3, 4, 2).map(Int::toDouble)

}

infix fun Double.fmod(other: Int) = ((this % other) + other) % other
