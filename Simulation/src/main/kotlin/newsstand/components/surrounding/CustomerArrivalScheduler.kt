package newsstand.components.surrounding

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.ExponentialRNG
import OSPRNG.UniformDiscreteRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.Customer
import newsstand.components.entity.CustomerType
import newsstand.components.entity.Group
import newsstand.constants.mc

abstract class CustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent,
    val terminal: Building,
    simID: Int
) : Scheduler(simID, mySim, parent) {
 var sex = 0
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
                println("${sex++} ${msg.convert().customer}")
                if (time != 0.0) {
                    hold(timeBetweenArrivals(), it)
                } else
                    assistantFinished(msg.createCopy())
            }

        else -> throw WrongMessageCode(msg)
    }

    abstract fun customerArrived(msg: Message)

    abstract fun timeBetweenArrivals(): Double

    protected abstract val means: List<Double>

    protected val generators by lazy { means.map { ExponentialRNG(3600.0 / it) } }

    protected val interval = 60 * 15.0

    protected val startTime = 60 * 60 * 16.0

     fun getIntervalIndex(simTime: Double): Int {
        for (i in 0 until means.size) {
            if (simTime in (startTime + (interval * i))..(startTime + (interval * (i + 1))))
                return i
        }
        return 0
    }

    protected fun createOneCustomer() = CustomerType.One(Customer(mySim().currentTime(), terminal))

    protected fun createGroup(sizeOfGroup: Int) = CustomerType.Group(
        Group(leader = Customer(mySim().currentTime(), terminal),
            family = List(sizeOfGroup) { Customer(mySim().currentTime(), terminal) }
        ))

    protected fun groupSize(): Int {
        val probability = rndGroupSizeGenerator.sample()
        val groupSize = when (probability) {
            in .0..0.6 -> 0
            in .6..0.8 -> 1
            in .8..0.95 -> 2
            in .95..1.0 -> 3
            else -> throw IllegalStateException("Probability is from 0-1")
        }
        return groupSize
    }

    private val rndGroupSizeGenerator = UniformDiscreteRNG(0, 1)

    private val rndSkipInterval = UniformDiscreteRNG(0, 1)

}