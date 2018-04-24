package newsstand

import OSPABA.Simulation
import newsstand.components.boss.BossAgent
import newsstand.components.minibus.MinibusAgent
import newsstand.components.surrounding.SurroundingAgent
import newsstand.components.terminal.TerminalAgent
import newsstand.components.entity.*

data class SimState(
    val terminalOneQueue: List<Customer>,
    val terminalTwoQueue: List<Customer>,
    val acrQueue: List<Customer>,
    val minibuses: List<Minibus>

)

class NewsstandSimualation : Simulation() {
    val boss = BossAgent(this)
    val surrouding = SurroundingAgent(this, boss)
    val terminal = TerminalAgent(this, boss)
    val minibus = MinibusAgent(this, boss)

    override fun prepareReplication() {
        super.prepareReplication()
        boss.start()
    }

    fun start() {
        simulate(1, 60 * 60 * 24 * 30.0 * 2)
    }

    fun state() = SimState(
        terminalOneQueue = terminal.terminalOne.queue.toList(),
        terminalTwoQueue = terminal.terminalTwo.queue.toList(),
        acrQueue = emptyList(),
        minibuses = minibus.minibuses
    )
}

fun main(args: Array<String>) {
    val s = NewsstandSimualation()
    s.onSimulationWillStart {
        println("start")
    }
    s.setSimSpeed(120.0, 1.0)
    s.onRefreshUI {
        println("""
        Terminal 1 :${s.terminal.terminalOne.queue.toList()}
        Terminal 2 : ${s.terminal.terminalTwo.queue.toList()}
                """.trimIndent())
        s.minibus.minibuses.forEachIndexed { index, minibus ->
            println("""
                ----
                Minibus $index
                ${minibus.source} to ${minibus.destination} @ ${minibus.leftAt}
                ${minibus.queue.toList()}
                ----
            """.trimIndent())
        }
        println("******************")
    }
    s.start()
}