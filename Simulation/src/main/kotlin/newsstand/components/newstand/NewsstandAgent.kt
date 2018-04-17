package newsstand.components.newstand

import OSPABA.Agent
import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.WStat
import abaextensions.addOwnMessages
import newsstand.components.entity.Customer
import newsstand.constants.id
import newsstand.constants.mc

class NewsstandAgent(
    mySim: Simulation,
    parent: Agent
) : Agent(id.NewsstandAgent, mySim, parent) {

    val queue = SimQueue<Customer>(WStat(mySim))
    var isServiceBusy = false

    init {
        NewsstandManager(mySim, this)
        ServiceCustomerProcess(mySim, this)
        addOwnMessages(mc.startCustomerService, mc.customerServiceEnded)
    }
}
