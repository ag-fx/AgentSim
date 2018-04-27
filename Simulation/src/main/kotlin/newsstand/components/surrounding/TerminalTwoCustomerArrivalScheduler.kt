package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.NewsstandSimualation
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.constants.id
import newsstand.constants.mc.newCustomer

class TerminalTwoCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.TerminalTwoCustomerArrivalScheduler, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .createCopy()
            .withCode(newCustomer)
            .let { hold(rnd.sample(), it) }

        newCustomer -> msg
            .createCopy()
            .convert()
            .let {
                hold(rnd.sample(), it)
                msg.convert().setNewCustomer(Building.TerminalTwo)
                assistantFinished(msg.createCopy())
            }

        else -> throw WrongMessageCode(msg)
    }

    private val rnd = ExponentialRNG(3600.0 / 19.0)

    override fun mySim() = super.mySim() as NewsstandSimualation

}