package newsstand.components.newstand

import OSPABA.Agent
import OSPABA.IdList.finish
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.WrongMessageCode
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.Message
import newsstand.constants.id
import newsstand.constants.mc

class NewsstandManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.NewsstandManager, mySim, myAgent) {

    override fun processMessage(message: MessageForm) = when (message.code()) {

        mc.startCustomerService -> startCustomerService(message as Message)

        mc.customerServiceEnded -> message
            .toAgent(id.SurroundingAgent)
            .withCode(mc.customerLeftSystem)
            .let { notice(it) }

        finish -> serviceHasEnded(message as Message)

        else -> throw WrongMessageCode(message)
    }

    private fun serviceHasEnded(msg: Message) = with(myAgent()) {
        isServiceBusy = false
        if (queue.isNotEmpty())
            startCustomerService(msg)
    }

    private fun startCustomerService(msg: Message) = with(myAgent()) {
        if (isServiceBusy)
            queue.enqueue(msg.customer)
        else {
            msg.customer = if(queue.isNotEmpty()) queue.pop() else msg.customer
            msg.toAgentsAssistant(myAgent(), id.ServiceCustomerProcess)
                .let {
                    myAgent().isServiceBusy = true
                    startContinualAssistant(it)
                }
        }
    }

    override fun myAgent() = super.myAgent() as NewsstandAgent
}