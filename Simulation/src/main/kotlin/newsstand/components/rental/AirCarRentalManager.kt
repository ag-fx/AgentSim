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
import newsstand.components.setEmployee
import newsstand.constants.id
import newsstand.constants.mc


/*
    class ZakaznikVRade

    class Jeden extends  ZakaznikVRade

    class Skupina extned ZakaznikVRade

    Queuue<ZakaznikVRade>


 */

class AirCarRentalManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.AirCarRentalManagerID, mySim, myAgent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        mc.airCarRentalMinibusArrival -> requestCustomer(msg)

        mc.getCustomerFromBusResponse -> handleBusCustomerArrival(msg)

        finish -> customerServiceEnded(msg)

        else -> {
            println(msg)
        }
    }

    private fun customerServiceEnded(msg: MessageForm) {
        msg.createCopy().toAgent(id.SurroundingAgent).withCode(mc.customerLeaving).let { notice(it) }
        if (myAgent().queue.isNotEmpty())
            tryToServeCustomer(msg)
    }

    private fun requestCustomer(msg: MessageForm) = msg
        .createCopy()
        .withCode(mc.getCustomerFromBusRequest)
        .toAgent(id.MinibusAgentID)
        .let { request(it) }

    private fun handleBusCustomerArrival(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .let {
            val customer = it.customer
            if (customer != null) {
                moveCustomerToQueue(it)
                tryToServeCustomer(it)
                requestCustomer(it)
            } else
                goToNextStop(it)
        }

    /** @see AssignEmployeeToCustomerAction  **/
    /** @see CustomerServiceScheduler  **/
    private fun tryToServeCustomer(msg: MessageForm) = myAgent()
        .employees
        .firstOrNull(Employee::isNotBusy)
        ?.let { employee ->
            msg
                .createCopy()
                .convert()
                .setEmployee(employee)
                .also { assignEmployeeToCustomer(it) }
                .toAgentsAssistant(myAgent(), id.CustomerServiceSchedulerID)
                .let { startContinualAssistant(it) }
        }


    /** @see AssignEmployeeToCustomerAction **/
    private fun assignEmployeeToCustomer(msg: Message) = msg
        .createCopy()
        .toAgentsAssistant(myAgent(), id.AssignEmployeeToCustomerAction)
        .let { execute(it) }

    /** @see MoveCustomerToQueueAction  **/
    private fun moveCustomerToQueue(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .withCode(mc.moveCustomerToQueueAtAirCarRental)
        .toAgentsAssistant(myAgent(), id.AirCarRentalMoveCustomerToQueueAction)
        .let { execute(it) }

    private fun goToNextStop(msg: Message) = msg
        .createCopy()
        .toAgent(id.MinibusMovementID)
        .withCode(mc.minibusGoTo)
        .let { notice(it) }


    override fun myAgent() = super.myAgent() as AirCarRentalAgent

}
