package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.NewsstandSimulation
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.constants.id
import newsstand.constants.mc.newCustomer


class TerminalTwoCustomerArrivalScheduler(
    mySim: Simulation,
    parent: Agent
) : CustomerArrivalScheduler(mySim, parent,Building.TerminalTwo, id.TerminalTwoCustomerArrivalScheduler) {

    override fun customerArrived(msg: Message) {
        msg.customer = createOneCustomer().value
    }

    override fun timeBetweenArrivals() = rnd.sample()!!
    private  val rnd = ExponentialRNG(3600.0 / 19.0)

}