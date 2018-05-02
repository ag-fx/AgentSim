package newsstand.components.minibus

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.UniformContinuousRNG
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
            .convert()
            .let {
                val timeToEnterBus = it.group!!.everyone().fold(0.0) { acc, _ -> acc + rndEnterBus.sample() }
                hold(timeToEnterBus, it)
            }

        mc.customerEnteredMinibus -> msg
            .createCopy()
            .convert()
            .let {
                val group = msg.createCopy().convert().group!!
                it.minibus!!.queue.push(group)
                it.group = null
                assistantFinished(it)
            }

        else -> TODO()
    }

    private val rndEnterBus = UniformContinuousRNG(12.0 - 2, 12.0 + 2)

    override fun myAgent() = super.myAgent() as MinibusAgent

}