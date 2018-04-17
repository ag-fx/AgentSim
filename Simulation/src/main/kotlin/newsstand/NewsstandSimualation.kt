package newsstand

import OSPABA.Simulation
import newsstand.components.boss.BossAgent
import newsstand.components.surrounding.SurroundingAgent
import newsstand.components.newstand.NewsstandAgent

class NewsstandSimualation : Simulation() {
    val boss = BossAgent(this)
    val surrouding = SurroundingAgent(this, boss)
    val newsstand = NewsstandAgent(this, boss)

var x =1
    override fun prepareReplication() {
        super.prepareReplication()
        boss.start()
        val a =   newsstand.queue
        val b =    newsstand.queue.lengthStatistic()
        newsstand.queue.lengthStatistic().clear()
        newsstand.queue.clear()
        println(x++)
    }

    override fun replicationFinished() {
        super.replicationFinished()
        newsstand.queue.lengthStatistic().mean().let { println(it) }

    }

    fun start() {
        simulate(10, 60 * 60 * 24 * 30.0*2)
    }
}

fun main(args: Array<String>) {
    val s = NewsstandSimualation()
    s.onSimulationWillStart {
        println("start")
    }
    s.start()
}