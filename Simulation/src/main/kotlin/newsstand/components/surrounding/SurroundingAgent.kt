package newsstand.components.surrounding

import OSPABA.Agent
import OSPABA.Simulation
import OSPStat.Stat
import abaextensions.addOwnMessages
import newsstand.constants.Clearable
import newsstand.constants.id
import newsstand.constants.mc

data class Counter(var total: Int = 0) : Clearable {

    fun add(x:Int){
        total+=x
    }
    fun inc() {
        total++
    }

    override fun clear() {
        total = 0
    }
}

class SurroundingAgent(
    mySim: Simulation,
    parent: Agent
) : Agent(id.SurroundingAgent, mySim, parent), Clearable {

    val timeInSystemIncoming = Stat()
    val timeInSystemLeaving  = Stat()
    val timeInSystemTotal    = Stat()

    val groupsT1  = Counter()
    val groupsT2  = Counter()
    val groupsAcr = Counter()

    init {
        SurroundingManager(mySim, this)
        TerminalOneCustomerArrivalScheduler(mySim, this)
        TerminalTwoCustomerArrivalScheduler(mySim, this)
        AirCarRentalCustomerArrivalScheduler(mySim, this)
        addOwnMessages(
            mc.init,
            mc.newCustomer,
            mc.customerLeaving,
            mc.clearLengthStat
        )
    }

    override fun clear() {
        timeInSystemIncoming.clear()
        timeInSystemLeaving .clear()
        timeInSystemTotal   .clear()
        listOf(groupsAcr, groupsT1, groupsT2).forEach(Counter::clear)
    }
}