package newsstand.components.boss

import OSPABA.*
import abaextensions.withCode
import newsstand.components.convert
import newsstand.constants.const

class AfterWarmUpScheduler(mySim: Simulation,
                           parent: Agent
) : Scheduler(-11, mySim, parent) {
    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        IdList.start -> msg
            .createCopy()
            .withCode(-11)
            .let { hold(const.WarmUpTime, it) }

        -11 -> msg
            .createCopy()
            .convert()
            .let { assistantFinished(it) }

        else -> TODO()
    }
}