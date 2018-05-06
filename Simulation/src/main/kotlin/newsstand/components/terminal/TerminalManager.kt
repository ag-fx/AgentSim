package newsstand.components.terminal

import OSPABA.Agent
import OSPABA.Manager
import OSPABA.MessageForm
import OSPABA.Simulation
import OSPStat.WStat
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

        mc.terminalOneMinibusArrival -> {
            msg.convert().minibus!!.addOccupancyStat()
            handleBusOnTerminal(myAgent().terminalOne, msg)
        }

        mc.terminalTwoMinibusArrival -> {
            msg.convert().minibus!!.addOccupancyStat()
            handleBusOnTerminal(myAgent().terminalTwo, msg)
        }

        mc.terminalThreeMinibusArrival -> {
            msg.convert().minibus!!.addOccupancyStat()
            requestPeopleFromMinibus(msg)
        }

        mc.enterMinibusResponse -> handleBusOnTerminal(msg)

        mc.getCustomerFromBusResponse ->
            if (msg.convert().group != null) {
                sendGroupHome(msg)
                requestPeopleFromMinibus(msg)
            } else
                msg.createCopy().toAgent(id.MinibusAgentID).withCode(mc.minibusGoTo).let { notice(it) }

        mc.clearLengthStat -> listOf(myAgent().terminalOne, myAgent().terminalTwo).forEach(Terminal::clearAfterWarmUp)


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
            Building.TerminalOne -> handleBusOnTerminal(myAgent().terminalOne, it)
            Building.TerminalTwo -> handleBusOnTerminal(myAgent().terminalTwo, it)
            else -> throw IllegalStateException()
        }
    }

    private fun getGroupedQueue(terminal: Terminal): Queue<Group> {
        val queue = terminal.queue
        val arrivalMap = mutableMapOf<Double, Group>()
        queue.forEach {
            arrivalMap[it.arrivedToSystem]?.add(it) ?: arrivalMap.put(it.arrivedToSystem, Group(it))
        }
        return LinkedList(arrivalMap.map { it.value })
    }

    private fun handleBusOnTerminal(terminal: Terminal, msg: MessageForm) {
        val minibus = msg.convert().minibus!!
        if (terminal.queue.isNotEmpty()) {
            val groups = getGroupedQueue(terminal)
            val group = groups.peek()
            if (group.size() <= minibus.freeSeats()) {
                group.everyone().forEach { terminal.queue.remove(it) }
                terminal.timeInQueueStat.addSample(mySim().currentTime() - group.startWaitingTimeTerminal)
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
    private fun addGroupToQueue(msg: MessageForm) = msg
        .convert().group!!
        .everyone()
        .forEach {
            msg
                .createCopy()
                .convert()
                .apply { oneCustomer = it }
                .toAgentsAssistant(myAgent(), id.AddToTerminalQueueAction)
                .let { execute(it) }
        }


    override fun myAgent() = super.myAgent() as TerminalAgent
}
