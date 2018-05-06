package newsstand.components.entity

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.Stat
import OSPStat.WStat
import newsstand.clearStat
import newsstand.constants.Clearable

abstract class Terminal(val building: Building, sim: Simulation? = null) : Clearable {

    val queue           = SimQueue<Customer>(WStat(sim))
    val timeInQueueStat = Stat()
    var totalCustomers  = 0

    fun clearAfterWarmUp(){
        queue.lengthStatistic().clear()
        timeInQueueStat.clear()
        totalCustomers = 0
    }

    override fun clear(){
        queue.clearStat()
        timeInQueueStat.clear()
        totalCustomers = 0
    }

}

class TerminalOne(sim: Simulation?) : Terminal(Building.TerminalOne, sim)

class TerminalTwo(sim: Simulation?) : Terminal(Building.TerminalTwo, sim)