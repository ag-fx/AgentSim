package newsstand.components.newstand

import OSPABA.Agent
import OSPABA.IdList.start
import OSPABA.MessageForm
import OSPABA.Simulation
import OSPABA.Process
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.constants.id
import newsstand.constants.mc

class ServiceCustomerProcess(
    simulation: Simulation,
    agent: Agent
) : Process(id.ServiceCustomerProcess, simulation, agent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        start -> message
            .withCode(mc.customerServiceEnded)
            .let { hold(rnd.sample(), it) }

        mc.customerServiceEnded -> assistantFinished(message)

        else -> throw WrongMessageCode(message)
    }

    companion object {
        val rnd = ExponentialRNG(5 / 1.0)
    }
}