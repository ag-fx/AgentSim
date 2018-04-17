package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.components.Message
import newsstand.components.entity.Customer
import newsstand.constants.id
import newsstand.constants.mc.newCustomer

class CustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.CustomerArrivalScheduler, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .withCode(newCustomer)
            .let { hold(rnd.sample(), it) }

        newCustomer -> msg
            .createCopy()
            .let {
                hold(rnd.sample(), it)
                (msg as Message).setNewCustomer()
                assistantFinished(msg)
            }

        else -> throw WrongMessageCode(msg)
    }

    companion object {
        val rnd = ExponentialRNG(60.0 / 10.0)
    }
}