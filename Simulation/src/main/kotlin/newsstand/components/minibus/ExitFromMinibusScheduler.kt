package newsstand.components.minibus

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.UniformContinuousRNG
import abaextensions.TestSample
import abaextensions.withCode
import newsstand.components.convert
import newsstand.constants.id
import newsstand.constants.mc

class ExitFromMinibusScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.ExitFromMinibusSchedulerID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {
        start -> msg
            .createCopy()
            .withCode(mc.customerExitedMinibus)
            .convert()
            .let {
                it.customer = null
                if (it.minibus!!.isNotEmpty())
                    hold(rndExitBus.sample(), it)
                else
                    assistantFinished(it)
            }

        mc.customerExitedMinibus -> msg
            .createCopy()
            .convert()
            .let {
                it.customer = it.minibus!!.queue.pop()!!
                assistantFinished(it)
            }

        else -> TODO()
    }

    private val rndExitBus = UniformContinuousRNG(8.0 - 4, 8.0 + 4)

    override fun myAgent() = super.myAgent() as MinibusAgent

}


