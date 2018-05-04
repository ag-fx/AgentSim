package newsstand.components.surrounding

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.ExponentialRNG
import OSPRNG.UniformContinuousRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.Customer
import newsstand.components.entity.Group
import newsstand.constants.const
import newsstand.constants.mc
import kotlin.math.roundToInt

abstract class CustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent,
    val terminal: Building,
    simID: Int
) : Scheduler(simID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .createCopy()
            .withCode(mc.newCustomer)
            .let { hold(timeBetweenArrivals(), it) }

        mc.newCustomer -> msg
            .createCopy()
            .convert()
            .let {
                val time = timeBetweenArrivals()
                customerArrived(msg.convert())
                if( mySim().currentTime() >= 60*60*20.5 + const.WarmUpTime)
                    assistantFinished(msg.createCopy())
                if (time != 0.0) {
                    hold(timeBetweenArrivals(), it)
                    assistantFinished(msg.createCopy())
                } else
                    assistantFinished(msg.createCopy())
            }

        else -> throw WrongMessageCode(msg)
    }

    private fun customerArrived(msg: Message) {
        msg.group = createGroup()
    }

    // does it work? yes. do I know why? no.
    // ¯\_(ツ)_/¯
    private fun timeBetweenArrivals(): Double {
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
    }

    protected abstract val means: List<Double>

    private val generators by lazy { means.map { ExponentialRNG(3600.0 / it) } }

    private val intervalGap = 60 * 15.0

    private fun createGroup() =
        Group(
            leader =                     Customer(mySim().currentTime(), terminal),
            family = List(groupSize()) { Customer(mySim().currentTime(), terminal) }.toMutableList(),
            startWaitingTimeTerminal  = if(terminal!=Building.AirCarRental) mySim().currentTime() else 0.0,
            startWaitingTimeCarRental = if(terminal==Building.AirCarRental) mySim().currentTime() else 0.0
        )

    private fun groupSize(): Int {
        val probability = rndGroupSizeGenerator.sample()
        return when (probability) {
            in 0.0..0.6  -> 0
            in 0.6..0.8  -> 1
            in 0.8..0.95 -> 2
            in 0.95..1.0 -> 3
            else -> throw IllegalStateException("Probability is from 0-1")
        }
    }

    private val rndGroupSizeGenerator = UniformContinuousRNG(0.0, 1.0)

}