package newsstand.components.minibus

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
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
            .withCode(mc.minibusArrivedToDestination)
            .convert()
            .let {
                it.minibus!!.apply {
                    isInDestination = false
                    leftAt = mySim().currentTime()
                }

                hold(it.minibus!!.source.secondsToNext(it.minibus!!.averageSpeed), it)
            }

        mc.minibusArrivedToDestination -> {
            val msg = msg.convert().createCopy()
            msg.minibus!!.apply {
                isInDestination = true
                source = destination
                destination = destination.nextStop()
                leftAt = mySim().currentTime()
            }
            assistantFinished(msg)
        }

        else -> throw WrongMessageCode(msg)
    }

}