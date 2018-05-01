package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.MessageForm
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import newsstand.components.Message
import newsstand.components.entity.Building
import newsstand.constants.id

class TerminalOneCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent, Building.TerminalOne, id.TerminalOneCustomerArrivalScheduler) {

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

//
//        if (nextArrivalTimeIndex == currentTimeIndex)
//            return generated
//        else {
//            val transofr = transform(generated, currentTimeIndex)
//            val tasa = (mySim().currentTime() + transofr).toTime()
//            return transofr//transform(generated, currentTimeIndex)
//        }
//    }
//
//    private fun transform(time: Double, currentTimeIndex: Int): Double {
////        val currentIndex = getIntervalIndex(mySim().currentTime() + time)  // tu som bez skaly
//        var i = currentTimeIndex+1
//        var hodnota = time
//        while (getIntervalIndex(hodnota + mySim().currentTime()) != i + 1) {
//            val indexBoundary = i * interval + interval + startTime
//            val s = hodnota + mySim().currentTime() - indexBoundary
//            val pomer = s * means[i] / means[i + 1]
//            hodnota = pomer
//            if (getIntervalIndex(hodnota + mySim().currentTime()) != i + 1)
//                i++
//        }
//        val indexBoundary = currentTimeIndex * interval + interval + startTime
//        val nextArrival = mySim().currentTime() + time
//        val overlap = nextArrival - indexBoundary
//        val firstScale = overlap * means[currentTimeIndex] / means[ currentTimeIndex + 1]
//        if (getIntervalIndex(nextArrival) == currentTimeIndex + 1)
//            return firstScale
//        else {
//             return transform(firstScale+indexBoundary,currentTimeIndex+1)
//        }
//    }
//
//    private fun getIntervalIndex(simTime: Double): Int {
//        for (i in 0 until means.size) {
//            if (simTime in (startTime + (interval * i))..(startTime + (interval * (i + 1))))
//                return i
//        }
//        return 0
//    }


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