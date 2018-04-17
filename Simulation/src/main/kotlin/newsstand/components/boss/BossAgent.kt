package newsstand.components.boss

import OSPABA.Agent
import OSPABA.Simulation
import abaextensions.addOwnMessages
import abaextensions.noticeManager
import abaextensions.toAgent
import abaextensions.withCode
import newsstand.components.Message
import newsstand.constants.id
import newsstand.constants.mc.customerArrivedToSystem
import newsstand.constants.mc.customerServiceEnded
import newsstand.constants.mc.init

class BossAgent(
    mySim: Simulation
) : Agent(id.BossAgent, mySim, null) {

    init {
        BossManager(mySim, this)
        addOwnMessages(init, customerArrivedToSystem, customerServiceEnded)
    }

    fun start() = Message(null, mySim())
        .toAgent(this)
        .withCode(init)
        .let { noticeManager(it) }
}
