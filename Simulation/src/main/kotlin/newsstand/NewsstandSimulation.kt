package newsstand

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.Stat
import newsstand.components.boss.BossAgent
import newsstand.components.entity.BusType
import newsstand.components.minibus.MinibusAgent
import newsstand.components.rental.AirCarRentalAgent
import newsstand.components.surrounding.SurroundingAgent
import newsstand.components.terminal.TerminalAgent
import newsstand.constants.Clearable
import newsstand.constants.const

data class Config(
    val minibuses: Int = 3,
    val employees: Int = 3,
    val busType: BusType = BusType.A
)


class NewsstandSimulation( config: Config = Config()) : Simulation(), Clearable {

    private val boss = BossAgent(this)
    private val surrounding = SurroundingAgent(this, boss)
    private val terminal = TerminalAgent(this, boss)
    private val minibus = MinibusAgent(this, boss, config)
    private val airCarRentalAgent = AirCarRentalAgent(this, boss, config.employees)

    val timeInSystemLeaving  = Stat()
    val timeInSystemIncoming = Stat()
    val timeInSystemTotal = Stat()

    val groupsT1  = Stat()
    val groupsT2  = Stat()
    val groupsAcr = Stat()

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

        surrounding.timeInSystemIncoming.mean().let(timeInSystemIncoming::addSample)
        surrounding.timeInSystemLeaving .mean().let(timeInSystemLeaving ::addSample)
        surrounding.timeInSystemTotal   .mean().let(timeInSystemTotal::addSample)
        clear()

    }

    override fun simulationFinished() {
        super.simulationFinished()
        groupsT1 .mean().let { println(it); }
        groupsT2 .mean().let { println(it); }
        groupsAcr.mean().let { println(it); }
        println()
        println("timeInSystemIncoming \t${timeInSystemIncoming.mean()/60.0}")
        println("timeInSystemLeaving  \t${timeInSystemLeaving.mean()/60.0}")
        println("timeInSystemTotal    \t${timeInSystemTotal.mean()/60.0}")
    }

    override fun clear() = listOf<Clearable>(boss, surrounding, terminal, minibus, airCarRentalAgent)
        .forEach(Clearable::clear)

    fun start(replicationCount: Int = 10_000, simEndTime: Double = 4.5 * 3600) {
        maxSimTime = simEndTime + const.WarmUpTime
        simulate(replicationCount, maxSimTime * 2)
    }


    fun getState() = SimState(
        timeInSystemIncoming = surrounding.timeInSystemIncoming,
        timeInSystemLeaving  = surrounding.timeInSystemLeaving,
        queueT1              = terminal.terminalOne.queue,
        timeStatQueueT1      = terminal.terminalOne.timeInQueueStat,
        queueT2              = terminal.terminalTwo.queue,
        timeStatQueueT2      = terminal.terminalTwo.timeInQueueStat,
        queueAcr             = airCarRentalAgent.queue,
        timeStatQueueAcr     = airCarRentalAgent.queueStat,
        queueAcrToT3         = airCarRentalAgent.queueToTerminal3,
        timeStatQueueAcrToT3 = airCarRentalAgent.queueToTerminal3Stat,
        acrEmployees         = airCarRentalAgent.employees,
        minibuses            = minibus.minibuses
    )
}


fun <E> SimQueue<E>.clearStat() {
    clear()
    lengthStatistic()?.clear()
}

fun main(args: Array<String>) {
    val s = NewsstandSimulation()
    s.start()
}

