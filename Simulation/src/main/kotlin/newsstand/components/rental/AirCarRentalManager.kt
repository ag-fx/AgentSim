package newsstand.components.rental

import OSPABA.Agent
import OSPABA.IdList.finish
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.log
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.Message
import newsstand.components.convert
import newsstand.components.entity.Employee
import newsstand.components.entity.isOneFree
import newsstand.components.setEmployee
import newsstand.constants.id
import newsstand.constants.mc

class AirCarRentalManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.AirCarRentalManagerID, mySim, myAgent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        mc.airCarRentalMinibusArrival -> requestCustomer(msg)

        mc.getCustomerFromBusResponse -> {
            if (msg.convert().customer == null)
                goToNextStop(msg)
            else {
                moveCustomerToQueue(msg)
                tryToServeCustomer(msg)
                requestCustomer(msg)
            }
        }

        finish -> serviceFinished(msg)

        else -> println(msg)

    }

    private fun tryToServeCustomer(msg: MessageForm) {
        val msg = msg.createCopy()
        if (myAgent().queue.isNotEmpty() && myAgent().employees.isOneFree()) {
            val employee = myAgent().employees.first { it.isNotBusy() }
            val customer = myAgent().queue.pop()
            employee.serveCustomer(customer)
            msg.convert().customer = customer
            serveCustomer(msg)
        }
    }

    private fun serviceFinished(msg: MessageForm) {
        sendCustomerHome(msg.createCopy())
        val msg = msg.createCopy()
        msg.convert().customer = null
        tryToServeCustomer(msg)
    }

    private fun sendCustomerHome(msg: MessageForm) = msg
        .createCopy()
        .toAgent(id.SurroundingAgent)
        .withCode(mc.customerLeaving)
        .let { notice(it) }

    private fun serveCustomer(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .toAgentsAssistant(myAgent(), id.CustomerServiceSchedulerID)
        .let { startContinualAssistant(it) }

    //region works
    private fun requestCustomer(msg: MessageForm) = msg
        .createCopy()
        .withCode(mc.getCustomerFromBusRequest)
        .toAgent(id.MinibusAgentID)
        .let { request(it) }


    /** @see MoveCustomerToQueueAction  **/
    private fun moveCustomerToQueue(msg: MessageForm) = msg
        .createCopy()
        .convert()
        .withCode(mc.moveCustomerToQueueAtAirCarRental)
        .toAgentsAssistant(myAgent(), id.AirCarRentalMoveCustomerToQueueAction)
        .let { execute(it) }


    private fun goToNextStop(msg: MessageForm) = msg
        .createCopy()
        .toAgent(id.MinibusMovementID)
        .withCode(mc.minibusGoTo)
        .let { notice(it) }

    //endregion

    override fun myAgent() = super.myAgent() as AirCarRentalAgent

}
