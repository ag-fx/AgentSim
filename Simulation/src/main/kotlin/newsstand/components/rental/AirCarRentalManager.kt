package newsstand.components.rental

import OSPABA.Agent
import OSPABA.IdList.finish
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Employee
import newsstand.constants.id
import newsstand.constants.mc

class AirCarRentalManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.AirCarRentalManagerID, mySim, myAgent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {
        mc.airCarRentalMinibusArrival -> exitFromMinibus(msg)

        finish -> when (msg.sender()) {
            is GetOffBusScheduler -> moveFromBusToQueue(msg)
            is CustomerServiceScheduler -> TODO()
            else -> TODO()
        }
        else -> {
        }//throw WrongMessageCode(msg)
    }

    private fun moveFromBusToQueue(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .let {
            val minibus = it.minibus!!
            val customer = minibus.queue.pop()
            myAgent().queue.push(customer)
            tryToServeCustomer(it)
            if (minibus.isNotEmpty())
                exitFromMinibus(it)
            else
                goToNextStop(it)
        }

    private fun goToNextStop(msg: Message) = msg
        .createCopy()
        .toAgent(id.MinibusMovementID)
        .withCode(mc.minibusGoTo)
        .let { notice(it) }

    private fun tryToServeCustomer(msg: Message) = myAgent()
        .employees
        .firstOrNull(Employee::isNotBusy)
        ?.let {

        }


    private fun exitFromMinibus(message: MessageForm) = message
        .createCopy()
        .toAgentsAssistant(myAgent(), id.GetOffBusAtCarRentalID)
        .let { startContinualAssistant(it) }

    override fun myAgent() = super.myAgent() as AirCarRentalAgent

}
