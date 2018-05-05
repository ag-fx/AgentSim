package newsstand.components.minibus

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import abaextensions.log
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.distanceToNext
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
            .withCode(mc.minibusArrivedToDestination)
            .convert()
            .let {
                hold(it.minibus!!.secondsToDestination(), it)
                log("Minibus start @ ${mySim().currentTime()}")
            }

        mc.minibusArrivedToDestination -> msg
            .createCopy()
            .convert()
            .let {
                it.minibus!!.apply {
                    isInDestination = true
                    meters += source.distanceToNext(this)
                    source = Building.TerminalOne
                    destination = source.nextStop(this)
                }
                log("Minibus arrived T1 @ ${mySim().currentTime()}")

                assistantFinished(it)
            }

        else -> throw IllegalStateException()
    }

}