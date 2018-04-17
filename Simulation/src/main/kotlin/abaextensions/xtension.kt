package abaextensions

import OSPABA.Agent
import OSPABA.MessageForm
import OSPDataStruct.SimQueue

class WrongMessageCode(messageForm: MessageForm) : IllegalArgumentException("${messageForm.sender().javaClass.simpleName} to ${messageForm.addressee().javaClass.simpleName} code :${messageForm.code()}")
//fun SimComponent.wrongMessage(messageForm: MessageForm): Nothing = throw WrongMessageCode("${messageForm.sender().javaClass.simpleName}  to ${messageForm.addressee().javaClass.simpleName} code :${messageForm.code()}")

fun MessageForm.toAgent(agentId: Int) = apply { setAddressee(mySim().findAgent(agentId)) }
fun MessageForm.toAgent(agent: Agent) = apply { setAddressee(agent) }
fun MessageForm.toAgentsAssistant(agentId: Int, assistantId: Int) = apply { setAddressee(mySim().findAgent(agentId).findAssistant(assistantId)) }
fun MessageForm.toAgentsAssistant(agent: Agent, assistantId: Int) = apply { setAddressee(agent.findAssistant(assistantId)) }
fun MessageForm.withCode(code: Int) = apply { setCode(code) }

fun Agent.addOwnMessages(vararg ids: Int) = ids.forEach { addOwnMessage(it) }
fun Agent.noticeManager(msg: MessageForm) = manager().notice(msg)

fun <E> SimQueue<E>.clearAll() {
    clear()
    lengthStatistic().clear()
    clear()
}