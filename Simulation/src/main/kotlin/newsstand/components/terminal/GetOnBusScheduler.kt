package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Scheduler
import OSPABA.Simulation
import OSPRNG.UniformContinuousRNG
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Terminal
import newsstand.constants.mc

abstract class GetOnBusScheduler(
    mySim: Simulation,
    parent: Agent,
    private val terminal: Terminal,
    id: Int
) : Scheduler(id, mySim, parent) {


    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        start -> msg
            .createCopy()
            .convert()
            .withCode(mc.customerEnteredBus)
            .let { hold(rndEnterBus.sample(), it) }

        mc.customerEnteredBus -> msg
            .createCopy()
            .convert()
            .let {
                val customer = terminal.queue.pop()
                val minibus = it.minibus!!
                minibus.queue.push(customer)
                assistantFinished(it)
            }

        else -> throw IllegalStateException()
    }


    override fun myAgent() = super.myAgent() as TerminalAgent

    abstract val rndEnterBus: UniformContinuousRNG
}
