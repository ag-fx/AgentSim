package newsstand.components.minibus

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.UniformContinuousRNG
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
                if (it.minibus!!.isNotEmpty()) {
                    val exitTime = it.minibus!!.queue.peek().everyone().fold(0.0) { acc, _ -> acc + rndExitBus.sample() }
                    hold(exitTime, it)
                } else {
                    val msg = it.createCopy().apply { group = null }
                    assistantFinished(msg)
                }
            }

        mc.customerExitedMinibus -> msg
            .createCopy()
            .convert()
            .let {
                it.group = it.minibus!!.queue.pop()!!
                assistantFinished(it)
            }

        else -> TODO()
    }

    private val rndExitBus = UniformContinuousRNG(6.0 - 4, 6.0 + 4)

    override fun myAgent() = super.myAgent() as MinibusAgent

}


