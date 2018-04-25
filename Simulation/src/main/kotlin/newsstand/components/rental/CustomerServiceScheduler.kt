package newsstand.components.rental

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.UniformContinuousRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
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
            .let { hold(rndServiceTime.sample(), it) }

        mc.customerServed -> msg
            .createCopy()
            .let { assistantFinished(it) }

        else -> throw WrongMessageCode(msg)
    }

    private val rndServiceTime = UniformContinuousRNG(6 * 60.0 - 4 * 60, 6.0 * 60 + 4 * 60)

}