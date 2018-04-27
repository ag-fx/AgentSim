package newsstand.components.minibus

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.UniformContinuousRNG
import abaextensions.withCode
import newsstand.components.convert
import newsstand.constants.id

class ExitFromMinibusScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.ExitFromMinibusSchedulerID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {
        start -> msg
            .createCopy()
            .withCode(789)
            .let { hold(rndExitBus.sample(), it) }

        789 -> msg
            .createCopy()
            .convert()
            .let {
                if (it.minibus!!.queue.isNotEmpty())
                    it.customer = it.minibus!!.queue.pop()!!
                else
                    it.customer = null
                assistantFinished(it)
            }

        else -> TODO()
    }

    val rndExitBus = UniformContinuousRNG(8.0 - 4, 8.0 + 4)

    override fun myAgent() = super.myAgent() as MinibusAgent

}