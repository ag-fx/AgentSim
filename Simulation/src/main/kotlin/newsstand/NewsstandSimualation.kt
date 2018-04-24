package newsstand

import OSPABA.Simulation
import newsstand.components.boss.BossAgent
import newsstand.components.surrounding.SurroundingAgent
import newsstand.components.terminal.TerminalAgent

class NewsstandSimualation : Simulation() {
    val boss = BossAgent(this)
    val surrouding = SurroundingAgent(this, boss)
    val terminal = TerminalAgent(this, boss)

    override fun prepareReplication() {
        super.prepareReplication()
        boss.start()
    }

    fun start() {
        simulate(1, 60 * 60 * 24 * 30.0*2)
    }
}

fun main(args: Array<String>) {
    val s = NewsstandSimualation()
    s.onSimulationWillStart {
        println("start")
    }
    s.start()
}