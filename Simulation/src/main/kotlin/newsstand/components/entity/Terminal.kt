package newsstand.components.entity

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.WStat

abstract class Terminal(val building: Building, sim: Simulation) {
    val queue = SimQueue<Customer>(WStat(sim))
}

class TerminalOne(sim: Simulation) : Terminal(Building.TerminalOne, sim)

class TerminalTwo(sim: Simulation) : Terminal(Building.TerminalTwo, sim)