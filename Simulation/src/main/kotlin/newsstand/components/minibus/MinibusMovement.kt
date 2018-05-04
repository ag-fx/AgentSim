package newsstand.components.minibus

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.log
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.distanceToNext
import newsstand.components.entity.nextStop
import newsstand.components.entity.secondsToNext
import newsstand.constants.id
import newsstand.constants.mc

class MinibusMovement(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.MinibusMovementID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .createCopy()
            .withCode(mc.minibusArrivedToDestination)
            .convert()
            .let {
                it.minibus!!.apply {
                    isInDestination = false
                    leftAt = mySim().currentTime()
                    destination = source.nextStop(this)
                }

                hold(it.minibus!!.secondsToDestination(), it)
                log("Minibus left ${it.minibus!!.source} @ ${mySim().currentTime()}")

            }

        mc.minibusArrivedToDestination -> {
            val msg = msg.convert().createCopy()
            msg.minibus!!.apply {
                kilometers+= source.distanceToNext(this)
                isInDestination = true
                source = destination
                destination = destination.nextStop(this)
            }
            log("Minibus arrived ${msg.minibus!!.source} @ ${mySim().currentTime()}")

            assistantFinished(msg)
        }

        else -> throw WrongMessageCode(msg)
    }

}