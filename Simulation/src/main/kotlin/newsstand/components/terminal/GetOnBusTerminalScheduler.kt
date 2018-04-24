package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.Simulation
import OSPRNG.UniformContinuousRNG
import newsstand.components.entity.Terminal
import newsstand.constants.id

class GetOnBusTerminalOneScheduler(
    mySim: Simulation,
    parent: Agent,
    terminal: Terminal
) : GetOnBusScheduler(mySim, parent, terminal, id.GetOnBusTerminalOne) {
    override val rndEnterBus = UniformContinuousRNG(12.0 - 2, 12.0 + 2)
}

class GetOnBusTerminalTwoScheduler(
    mySim: Simulation,
    parent: Agent,
    terminal: Terminal
) : GetOnBusScheduler(mySim, parent, terminal, id.GetOnBusTerminalTwo) {
    override val rndEnterBus = UniformContinuousRNG(12.0 - 2, 12.0 + 2)
}