package newsstand.components.rental

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.UniformContinuousRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.constants.id
import newsstand.constants.mc


class GetOffBusScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.GetOffBusAtCarRentalID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .createCopy()
            .withCode(mc.customerGotOffBus)
            .let { hold(rndGetOffBus.sample(), it) }

        mc.customerGotOffBus -> msg
            .createCopy()
            .let { assistantFinished(it) }

        else -> throw WrongMessageCode(msg)
    }

    private val rndGetOffBus = UniformContinuousRNG(8.0 - 4, 8.0 + 4)

}


