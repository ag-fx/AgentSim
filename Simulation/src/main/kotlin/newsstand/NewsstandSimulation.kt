package newsstand

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.Stat
import abaextensions.toSimQueue
import newsstand.components.boss.BossAgent
import newsstand.components.minibus.MinibusAgent
import newsstand.components.rental.AirCarRentalAgent
import newsstand.components.surrounding.Counter
import newsstand.components.surrounding.SurroundingAgent
import newsstand.components.terminal.TerminalAgent
import newsstand.constants.Clearable
import newsstand.constants.const

data class Config(
    val minibuses: Int = 2,
    val employees: Int = 1,
    val bustype: BusType = BusType.A
)

enum class BusType { A, B, C }

class NewsstandSimulation(val config: Config = Config()) : Simulation(), Clearable {

    private val boss = BossAgent(this)
    private val surrounding = SurroundingAgent(this, boss)
    private val terminal = TerminalAgent(this, boss)
    private val minibus = MinibusAgent(this, boss, config)
    private val airCarRentalAgent = AirCarRentalAgent(this, boss, config.employees)

    val groupsT1  =Stat()
    val groupsT2  =Stat()
    val groupsAcr =Stat()

    internal var maxSimTime: Double = 0.0
    var warmedUp = false

    override fun prepareReplication() {
        super.prepareReplication()
        clear()
        warmedUp = false
        boss.start()
    }


    override fun replicationFinished() {
        super.replicationFinished()
        surrounding.groupsT1.total .let { groupsT1 .addSample(it.toDouble()) }
        surrounding.groupsT2.total .let { groupsT2 .addSample(it.toDouble()) }
        surrounding.groupsAcr.total.let { groupsAcr.addSample(it.toDouble()) }
        clear()
    }

    override fun simulationFinished() {
        super.simulationFinished()
        groupsT1 .mean().let { println(it); }
        groupsT2 .mean().let { println(it); }
        groupsAcr.mean().let { println(it); }
    }

    override fun clear() = listOf<Clearable>(boss, surrounding, terminal, minibus, airCarRentalAgent)
        .forEach(Clearable::clear)

    fun start(replicationCount: Int = 1000, simEndTime: Double = 4.5 * 3600) {
        maxSimTime = simEndTime + const.WarmUpTime
        simulate(replicationCount, maxSimTime * 2)
    }


    fun getState() = SimState(
        queueT1 = terminal.terminalOne.queue,
        queueT2 = terminal.terminalTwo.queue,
        timeStatQueueT1 = terminal.terminalOne.timeInQueue,
        timeStatQueueT2 = terminal.terminalTwo.timeInQueue,
        queueAcr        = airCarRentalAgent.queue,
        queueAcrToT3 = airCarRentalAgent.queueToTerminal3,
        acrEmployees = airCarRentalAgent.employees,
        minibuses = minibus.minibuses
    )
}

fun Simulation.isWarmedUp() = (this as NewsstandSimulation).currentTime() > const.WarmUpTime

fun <E> SimQueue<E>.clearStat() {
    clear()
    lengthStatistic()?.clear()
}

fun main(args: Array<String>) {
    val s = NewsstandSimulation()
    s.start()
    //  println("21.71416177 milan")
}//19.58008985

