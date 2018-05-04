package newsstand.components.boss

import OSPABA.*
import abaextensions.addOwnMessages
import abaextensions.noticeManager
import abaextensions.toAgent
import abaextensions.withCode
import newsstand.Config
import newsstand.components.Message
import newsstand.components.convert
import newsstand.constants.Clearable
import newsstand.constants.id
import newsstand.constants.mc
import newsstand.constants.mc.customerArrivalTerminalOne
import newsstand.constants.mc.customerArrivalTerminalTwo
import newsstand.constants.mc.init

class BossAgent(
    val mySim: Simulation,
    config: Config
) : Agent(id.BossAgent, mySim, null),Clearable {


    init {
        BossManager(mySim, this,config)
        TestSchedler(mySim, this)
        AfterWarmUpScheduler(mySim, this)
        addOwnMessages(init, -1,-11)
    }

    fun start() = Message(null, mySim)
        .toAgent(this)
        .withCode(init)
        .let { noticeManager(it) }

    override fun clear() {
    }
}

