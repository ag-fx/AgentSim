package newsstand.components.minibus

import OSPABA.*
import OSPRNG.ExponentialRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.components.entity.secondsToNext
import newsstand.constants.id
import newsstand.constants.mc

class MinibusMovement(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.MinibusMovementID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        IdList.start -> msg
            .withCode(mc.minibusGoTo)
            .convert()
            .let { hold(it.minibus!!.source.secondsToNext(it.minibus!!.averageSpeed), it) }

        mc.minibusGoTo -> {
            val msg = msg.convert()
            val minibus = msg.minibus!!
            when (minibus.destination) {
                Building.TerminalOne -> TODO()
                Building.TerminalTwo -> TODO()
                Building.AirCarRental -> TODO()
            }
//            val cpy = msg.createCopy()
//            hold(rnd.sample(), cpy)
//
//
//            msg.convert().setNewCustomer(Building.TerminalOne)
//            assistantFinished(msg)
        }

        else -> throw WrongMessageCode(msg)
    }

    private val rnd = ExponentialRNG(3600.0 / 43.0)
}