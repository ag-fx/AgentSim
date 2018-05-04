package newsstand.components.entity

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.Stat
import OSPStat.WStat
import newsstand.clearStat
import newsstand.constants.Clearable

abstract class Terminal(val building: Building, val sim: Simulation? = null) : Clearable {

    val queue       = SimQueue<Customer>(WStat(sim))
    val timeInQueue = Stat()
    var totalCustomers = 0

    override fun clear(){
        queue.clearStat()
        timeInQueue.clear()
        totalCustomers = 0
    }

}

class TerminalOne(sim: Simulation?) : Terminal(Building.TerminalOne, sim)

class TerminalTwo(sim: Simulation?) : Terminal(Building.TerminalTwo, sim)