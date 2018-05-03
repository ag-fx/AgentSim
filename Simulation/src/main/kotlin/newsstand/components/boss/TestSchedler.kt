package newsstand.components.boss

import OSPABA.*
import abaextensions.withCode
import newsstand.components.convert

class TestSchedler(mySim: Simulation,
                   parent: Agent
) : Scheduler(-10, mySim, parent) {
    override fun processMessage(msg: MessageForm) = when (msg.code()) {

        IdList.start -> msg
            .createCopy()
            .withCode(-1)
            .let { hold(0.0, it) }

        -1 -> msg
            .createCopy()
            .convert()
            .let { assistantFinished(it) }

        else -> TODO()
    }
}

