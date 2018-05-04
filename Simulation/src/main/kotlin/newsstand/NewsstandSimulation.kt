package newsstand

import OSPABA.Simulation
import OSPDataStruct.SimQueue
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
    val busType: BusType = BusType.A,
    val slowDownAfterWarmUp: Boolean = false,
    val replicationCount: Int = 1000
)


@Suppress("MemberVisibilityCanBePrivate")
class NewsstandSimulation(val config: Config = Config()) : Simulation(), Clearable {

    private val boss = BossAgent(this, config)
    private val surrounding = SurroundingAgent(this, boss)
    private val terminal = TerminalAgent(this, boss)
    private val minibus = MinibusAgent(this, boss, config)
    private val airCarRentalAgent = AirCarRentalAgent(this, boss, config.employees)

    val timeInSystemLeaving = Result("Čas v systéme odchádzajúci",ResultType.Time)
    val timeInSystemIncoming = Result("Čas v systéme prichádzajúci",ResultType.Time)
    val timeInSystemTotal = Result("Čas v systéme spoločný",ResultType.Time)
    val queueT1 = Result("Dĺžka fronty Terminál 1")
    val timeStatQueueT1 = Result("Čas čakania Terminál 1",ResultType.Time)
    val queueT2 = Result("Dĺžka fronty Terminál 2")
    val timeStatQueueT2 = Result("Čas čakania Terminál 2",ResultType.Time)
    val queueAcr = Result("Dĺžka fronty na obsluhu")
    val timeStatQueueAcr = Result("Čas čakania na obsluhu",ResultType.Time)

    val queueAcrToT3 = Result("Dĺžka fronty na odvoz")
    val timeStatQueueAcrToT3 = Result("Čas čakania na odvoz",ResultType.Time)
    val acrQueueLength = Result("")

    //TODO Vyťaženosť minibusu
    //TODO Vyťaženosť obsluhujúceho
    //TODO Čas čakania prichádzajúcich
    //TODO Čas čakania odchádzajúcich
    //TODO Trvanie jednej prevádzky
    //TODO Počet najazdených km
    //TODO Náklady vodiči
    //TODO Náklady obsluhujúci
    //TODO Náklady celkové

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
        val repState = getState()
        repState.timeInSystemIncoming.mean().let(timeInSystemIncoming::addSample)
        repState.timeInSystemLeaving.mean().let(timeInSystemLeaving::addSample)
        repState.timeInSystemTotal.mean().let(timeInSystemTotal::addSample)
        repState.queueT1.mean().let { queueT1.addSample(it) }
        repState.timeStatQueueT1.mean().let { timeStatQueueT1.addSample(it) }
        repState.queueT2.mean().let { queueT2.addSample(it) }
        repState.timeStatQueueT2.mean().let { timeStatQueueT2.addSample(it) }
        repState.queueAcr.mean().let { queueAcr.addSample(it) }
        repState.timeStatQueueAcr.mean().let { timeStatQueueAcr.addSample(it) }
        repState.queueAcrToT3.mean().let { queueAcrToT3.addSample(it) }
        repState.timeStatQueueAcrToT3.mean().let { timeStatQueueAcrToT3.addSample(it) }

        airCarRentalAgent.queue.lengthStatistic().mean().let(acrQueueLength::addSample)
        clear()
    }

    override fun simulationFinished() {
        super.simulationFinished()
        println("timeInSystemIncoming \t${timeInSystemIncoming.mean()}")
        println("timeInSystemLeaving  \t${timeInSystemLeaving.mean()}")
        println("timeInSystemTotal    \t${timeInSystemTotal.mean()}")
        println("acrQueueLength \t${acrQueueLength.mean()}")
    }

    override fun clear() = listOf<Clearable>(boss, surrounding, terminal, minibus, airCarRentalAgent)
        .forEach(Clearable::clear)

    fun start(simEndTime: Double = 4.5 * 3600) {
        maxSimTime = simEndTime + const.WarmUpTime
        simulate(config.replicationCount, maxSimTime * 2)
    }


    fun getState() = SimState(
        timeInSystemIncoming = surrounding.timeInSystemIncoming,
        timeInSystemLeaving = surrounding.timeInSystemLeaving,
        timeInSystemTotal = surrounding.timeInSystemTotal,
        queueT1 = terminal.terminalOne.queue,
        timeStatQueueT1 = terminal.terminalOne.timeInQueueStat,
        queueT2 = terminal.terminalTwo.queue,
        timeStatQueueT2 = terminal.terminalTwo.timeInQueueStat,
        queueAcr = airCarRentalAgent.queue,
        timeStatQueueAcr = airCarRentalAgent.queueStat,
        queueAcrToT3 = airCarRentalAgent.queueToTerminal3,
        timeStatQueueAcrToT3 = airCarRentalAgent.queueToTerminal3Stat,
        acrEmployees = airCarRentalAgent.employees,
        minibuses = minibus.minibuses
    )

    val allResults = listOf(
        timeInSystemLeaving,
        timeInSystemIncoming,
        timeInSystemTotal,
        spacer(),
        queueT1,
        timeStatQueueT1,
        queueT2,
        timeStatQueueT2,
        spacer(),
        queueAcr,
        timeStatQueueAcr,
        queueAcrToT3,
        timeStatQueueAcrToT3,
        acrQueueLength
    )
    private fun spacer(name:String="") = Result(name,ResultType.Spacer)
}


fun <E> SimQueue<E>.clearStat() {
    clear()
    lengthStatistic()?.clear()
}

fun <E> SimQueue<E>.mean() = lengthStatistic().mean()

fun main(args: Array<String>) {
    val s = NewsstandSimulation()
    s.start()
}