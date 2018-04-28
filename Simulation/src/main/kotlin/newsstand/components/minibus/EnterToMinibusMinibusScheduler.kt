package newsstand.components.minibus

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.UniformContinuousRNG
import abaextensions.TestSample
import abaextensions.withCode
import newsstand.components.convert
import newsstand.constants.id
import newsstand.constants.mc

class EnterToMinibusMinibusScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.EnterToMinibusSchedulerID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .createCopy()
            .withCode(mc.customerEnteredMinibus)
            .let { hold(rndEnterBus.sample(), it) }

        mc.customerEnteredMinibus -> msg
            .createCopy()
            .convert()
            .let {
                it.minibus!!.queue.push(it.customer!!)
                assistantFinished(it)
            }

        else -> TODO()
    }

    private val rndEnterBus = UniformContinuousRNG(12.0 - 2, 12.0 + 2)

    override fun myAgent() = super.myAgent() as MinibusAgent

}