package newsstand.components.boss

import OSPABA.Agent
import OSPABA.Simulation
import abaextensions.addOwnMessages
import abaextensions.noticeManager
import abaextensions.toAgent
import abaextensions.withCode
import newsstand.components.Message
import newsstand.constants.id
import newsstand.constants.mc
import newsstand.constants.mc.customerArrivalTerminalOne
import newsstand.constants.mc.customerArrivalTerminalTwo
import newsstand.constants.mc.init

class BossAgent(
    val mySim: Simulation
) : Agent(id.BossAgent, mySim, null) {

    init {
        BossManager(mySim, this)
        addOwnMessages(init)
    }

    fun start() = Message(null, mySim)
        .toAgent(this)
        .withCode(init)
        .let { noticeManager(it) }
}
