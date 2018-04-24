package newsstand.components.surrounding

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
import newsstand.constants.id
import newsstand.constants.mc.newCustomer

class TerminalOneCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.TerminalOneCustomerArrivalScheduler, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .withCode(newCustomer)
            .let { hold(rnd.sample(), it) }

        newCustomer -> {
            val cpy = msg.createCopy()
            hold(rnd.sample(), cpy)
            msg.convert().setNewCustomer(Building.TerminalOne)
            assistantFinished(msg)
        }

        else -> throw WrongMessageCode(msg)
    }

    private val rnd = ExponentialRNG(3600.0 / 43.0)
}