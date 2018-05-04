package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import abaextensions.toAgent
import abaextensions.toAgentsAssistant
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.Group
import newsstand.components.entity.Terminal
import newsstand.constants.id
import newsstand.constants.mc
import java.util.*

class TerminalManager(
    mySim: Simulation,
    myAgent: Agent
) : Manager(id.TerminalManagerID, mySim, myAgent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        mc.customerArrivalTerminalOne,
        mc.customerArrivalTerminalTwo -> addGroupToQueue(msg)

        mc.terminalOneMinibusArrival -> handleBusOnTerminal(myAgent().terminalOne, msg)

        mc.terminalTwoMinibusArrival -> handleBusOnTerminal(myAgent().terminalTwo, msg)

        mc.terminalThreeMinibusArrival -> requestPeopleFromMinibus(msg)

        mc.enterMinibusResponse -> handleBusOnTerminal(msg)

        mc.getCustomerFromBusResponse ->
            if (msg.convert().group != null) {
                sendGroupHome(msg)
                requestPeopleFromMinibus(msg)
            } else
                msg.createCopy().toAgent(id.MinibusAgentID).withCode(mc.minibusGoTo).let { notice(it) }

        mc.clearLengthStat -> {
            myAgent().terminalOne.queue.lengthStatistic().clear()
            myAgent().terminalTwo.queue.lengthStatistic().clear()
        }

        else -> {
        }
    }


    private fun requestPeopleFromMinibus(msg: MessageForm) = msg
        .createCopy()
        .withCode(mc.getCustomerFromBusRequest)
        .toAgent(id.MinibusAgentID)
        .let { request(it) }

    private fun sendGroupHome(msg: MessageForm) = msg
        .createCopy()
        .withCode(mc.customerLeaving)
        .toAgent(id.SurroundingAgent)
        .let { notice(it) }

    private fun handleBusOnTerminal(msg: MessageForm) = msg.createCopy().convert().let {
        when (it.building!!) {
            Building.TerminalOne -> handleBusOnTerminal(myAgent().terminalOne, msg)
            Building.TerminalTwo -> handleBusOnTerminal(myAgent().terminalTwo, msg)
            else -> throw IllegalStateException()
        }
    }

    private fun handleBusOnTerminal(terminal: Terminal, msg: MessageForm) {
        val queue = terminal.queue
        val minibus = msg.createCopy().convert().minibus!!
        val mapa = mutableMapOf<Double, Group>()
        queue.forEach {
            mapa[it.arrivedToSystem]?.add(it) ?: mapa.put(it.arrivedToSystem, Group(it))
        }

        if (terminal.queue.isNotEmpty()) {
            val groupx = mapa.map { it.value }.toMutableList() //as Queue<Group>
            val groups: Queue<Group> = LinkedList(groupx)
            val group = groups.peek()
            if (group.size() <= minibus.freeSeats()) {
                group.everyone().forEach { queue.remove(it) }
                terminal.timeInQueue.addSample(mySim().currentTime() - group.startWaitingTimeTerminal)
                requestLoading(msg, group, terminal)
            }
        } else {
            msg.createCopy().toAgent(id.MinibusAgentID).withCode(mc.minibusGoTo).let { notice(it) }
        }

    }

    private fun requestLoading(msg: MessageForm, group: Group, terminal: Terminal) = msg
        .createCopy()
        .convert()
        .apply {
            this.group = group
            this.building = terminal.building
        }
        .withCode(mc.enterMinibusRequest)
        .toAgent(id.MinibusAgentID)
        .let { request(it) }

    /** @see AddToTerminalQueueAction **/
    private fun addGroupToQueue(msg: MessageForm) {
        msg.convert().group!!.everyone().forEach {
            msg
                .createCopy()
                .convert()
                .apply { oneCustomer = it }
                .toAgentsAssistant(myAgent(), id.AddToTerminalQueueAction)
                .let { execute(it) }
        }
    }

    override fun myAgent() = super.myAgent() as TerminalAgent
}
