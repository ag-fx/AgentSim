package newsstand.components.rental

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.UniformContinuousRNG
import abaextensions.TestSample
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.components.convert
import newsstand.constants.id
import newsstand.constants.mc

class CustomerServiceScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.CustomerServiceSchedulerID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {
        start -> msg
            .createCopy()
            .withCode(mc.customerServed)
            .let {
                hold(rndServiceTime.sample(), it)
            }

        mc.customerServed -> msg
            .createCopy()
            .convert()
            .let {
                val customer = it.customer!!
                val employee = myAgent().employees.first { it.serving == (customer) }
                employee.done()
                assistantFinished(it)
            }

        else -> throw WrongMessageCode(msg)
    }

    private val rndServiceTime = UniformContinuousRNG((6 * 60.0) - (4 * 60), (6.0 * 60) + (4 * 60))
    override fun myAgent() = super.myAgent() as AirCarRentalAgent
}