package newsstand

import OSPABA.Simulation
import newsstand.components.boss.BossAgent
import newsstand.components.entity.Customer
import newsstand.components.entity.Employee
import newsstand.components.entity.Minibus
import newsstand.components.entity.Terminal
import newsstand.components.minibus.MinibusAgent
import newsstand.components.rental.AirCarRentalAgent
import newsstand.components.surrounding.SurroundingAgent
import newsstand.components.terminal.TerminalAgent

data class SimState(
    val terminals: List<Terminal>,
    val acrQueue: List<Customer>,
    val acrEmployees: List<Employee>,
    val minibuses: List<Minibus>

)

class NewsstandSimualation : Simulation() {

    val boss = BossAgent(this)
    val surrouding = SurroundingAgent(this, boss)
    val terminal = TerminalAgent(this, boss)
    val minibus = MinibusAgent(this, boss)
    val airCarRentalAgent = AirCarRentalAgent(this, boss)


    override fun prepareReplication() {
        super.prepareReplication()
        boss.start()
    }

    fun start() {
        simulate(1, 60 * 60 * 24 * 30.0 * 2)
    }

    fun getState() = SimState(
        terminals = listOf(terminal.terminalOne, terminal.terminalTwo),
        acrQueue = airCarRentalAgent.queue,
        acrEmployees = airCarRentalAgent.employees,
        minibuses = minibus.minibuses
    )
}

fun main(args: Array<String>) {
    val s = NewsstandSimualation()
    s.start()
}