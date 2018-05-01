package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import newsstand.components.Message
import newsstand.components.entity.Building
import newsstand.constants.id
import kotlin.math.pow
import kotlin.math.sqrt

class TerminalOneCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.TerminalOne, id.TerminalOneCustomerArrivalScheduler) {

    override fun customerArrived(msg: Message) {
        msg.customer = createOneCustomer().value
    }

    override fun timeBetweenArrivals(): Double {
        val time = mySim().currentTime()
        val currentTimeIndex = getIntervalIndex(mySim().currentTime())
        val generated = generators[currentTimeIndex].sample()
        if (mySim().currentTime() + generated > 60 * 60 * 20.5)
            return 0.0
        val nextArrivalTimeIndex = getIntervalIndex(mySim().currentTime() + generated)
        if (currentTimeIndex == nextArrivalTimeIndex)
            return generated
        else {
            return transform(generated, currentTimeIndex)
        }
    }

    private fun transform(time: Double, currentTimeIndex: Int): Double {
        val indexBoundary = currentTimeIndex * interval + interval// + startTime
        val overlap = sqrt((time-indexBoundary).pow(2))
        if(currentTimeIndex==17) return 0.0
        val scaled = overlap * means[currentTimeIndex] / means[currentTimeIndex + 1]
        val indexe = getIntervalIndex(indexBoundary+startTime+overlap)
        if (indexe == currentTimeIndex + 1)
            return overlap * means[currentTimeIndex] / means[currentTimeIndex + 1]
        else {
            val nextBoundary = indexBoundary + interval

            return transform(scaled + indexBoundary, currentTimeIndex + 1)
        }
    }


    /*
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
            }
            val gen = (generated).toTime()
            if (generated - mySim().currentTime() < 0)
                return 0.0
            else
                return generated - mySim().currentTime()
        }
    */
    private val rnd = ExponentialRNG(3600.0 / 43.0)

    override val means = listOf(4, 8, 12, 15, 18, 14, 13, 10, 4, 6, 10, 14, 16, 15, 7, 3, 4, 2).map(Int::toDouble)

}

fun Double.toTime() = MyTime().apply { actualTime = this@toTime }.toString()


class MyTime {
    var actualTime: Double = 0.0
        set(value) {
            field = value
            calculateTime(value)
        }

    private var d: Int = 0
    private var h: Int = 0
    private var m: Int = 0
    private var s: Double = 0.toDouble()
    private val lengthOfDayHours = 24.0

    private fun calculateTime(actualTime: Double) {
        s = 0.0
        m = 0
        h = 0
        d = 0

        h = Math.floor(actualTime / 3600 % this.lengthOfDayHours).toInt()

        m = Math.floor((actualTime / 3600 % lengthOfDayHours - h) * 60).toInt()
        s = actualTime % 60

        d = Math.floor(actualTime / 3600 / this.lengthOfDayHours).toInt()

    }

    override fun toString() =
        if (d > 0) "Deň $d" + " " + h + ":" + m + ":" + String.format("%.0f", s) + " " else "" + h + ":" + m + ":" + String.format("%.0f", s) + " "


}