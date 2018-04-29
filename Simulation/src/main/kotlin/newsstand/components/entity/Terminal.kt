package newsstand.components.entity

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.Stat
import OSPStat.WStat

abstract class Terminal(val building: Building, val sim: Simulation? = null) {
    val queue = SimQueue<Customer>(WStat(sim))
    val timeInQueue = Stat()
}

class TerminalOne(sim: Simulation?) : Terminal(Building.TerminalOne, sim)

class TerminalTwo(sim: Simulation?) : Terminal(Building.TerminalTwo, sim)