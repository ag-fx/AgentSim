package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.Simulation
import OSPRNG.UniformContinuousRNG
import newsstand.components.entity.Terminal
import newsstand.constants.id

class EnterBusTerminalOneScheduler(
    mySim: Simulation,
    parent: Agent,
    terminal: Terminal
) : EnterBusScheduler(mySim, parent, terminal, id.GetOnBusTerminalOne) {

    override val rndEnterBus = UniformContinuousRNG(12.0 - 2, 12.0 + 2)

}

class EnterBusTerminalTwoScheduler(
    mySim: Simulation,
    parent: Agent,
    terminal: Terminal
) : EnterBusScheduler(mySim, parent, terminal, id.GetOnBusTerminalTwo) {

    override val rndEnterBus = UniformContinuousRNG(12.0 - 2, 12.0 + 2)

}