package newsstand.components.terminal

import OSPABA.Action
import OSPABA.MessageForm
import OSPABA.*
import newsstand.components.convert
import newsstand.components.entity.*
import newsstand.constants.id


class EnterBusT1(mySim: Simulation, parent: Agent, terminal: Terminal) : EnterBusAction(mySim, parent, terminal,id.EnterBusActionT1)

class EnterBusT2(mySim: Simulation, parent: Agent, terminal: Terminal) : EnterBusAction(mySim, parent, terminal,id.EnterBusActionT2)

abstract class EnterBusAction(
    mySim: Simulation,
    parent: Agent,
    private val terminal: Terminal,
    id: Int
) : Action(id, mySim, parent) {

    override fun execute(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .let {
            val customer = terminal.queue.pop()
            terminal.timeInQueue.addSample(mySim().currentTime() - customer.arrivedToSystem)
            it.minibus!!.queue.push(customer)
        }

}