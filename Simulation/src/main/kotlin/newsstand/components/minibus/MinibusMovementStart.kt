package newsstand.components.minibus

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.nextStop
import newsstand.constants.id
import newsstand.constants.mc

class MinibusMovementStart(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.MinibusMovementStartID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .createCopy()
            .withCode(mc.terminalOneMinibusArrival)
            .convert()
            .let { hold(it.minibus!!.secondsToDestination().also { println(it) }, it) }

        mc.terminalOneMinibusArrival -> msg
            .createCopy()
            .convert()
            .let {
                it.minibus!!.apply {
                    isInDestination = true
                    source = Building.TerminalOne
                    destination = source.nextStop()
                    leftAt = mySim().currentTime()
                }
                assistantFinished(it)
            }

        else -> throw IllegalStateException()
    }

}