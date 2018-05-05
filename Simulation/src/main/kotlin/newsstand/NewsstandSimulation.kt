package newsstand

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import newsstand.components.boss.BossAgent
import newsstand.components.entity.Employee
import newsstand.components.entity.Minibus
import newsstand.components.minibus.MinibusAgent
import newsstand.components.rental.AirCarRentalAgent
import newsstand.components.surrounding.SurroundingAgent
import newsstand.components.terminal.TerminalAgent
import newsstand.constants.Clearable
import newsstand.constants.const

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

    val employeeOccupancy = Result("Vyťaženosť obsluhujúceho")

    val busOccupancy    = Result("Vyťaženosť autobusov")
    val busKiloneters   = Result("Počet najazdených km")
    val priceKilometers = Result("Cena za najazdene km")
    val priceBusDriver  = Result("Cena prace soferov")
    val priceService    = Result("Cena prace obsluhujúci")

    //TODO Trvanie jednej prevádzky
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
        repState.timeStatQueueT1.mean().let { timeStatQueueT1.addSample(it/60) }
        repState.queueT2.mean().let { queueT2.addSample(it) }
        repState.timeStatQueueT2.mean().let { timeStatQueueT2.addSample(it/60) }
        repState.queueAcr.mean().let { queueAcr.addSample(it) }
        repState.timeStatQueueAcr.mean().let { timeStatQueueAcr.addSample(it/60) }
        repState.queueAcrToT3.mean().let { queueAcrToT3.addSample(it) }
        repState.timeStatQueueAcrToT3.mean().let { timeStatQueueAcrToT3.addSample(it/60) }
        repState.employeeOccupancy.let { employeeOccupancy.addSample(it) }
        repState.busOccupancy.let { busOccupancy.addSample(it) }
        minibus.minibuses.map { it.meters / 1000 }.sum().let { busKiloneters.addSample(it) }
        minibus.minibuses.map { it.meters / 1000 * it.pricePerKm }.sum().let { priceKilometers.addSample(it) }
        minibus.minibuses.map { (it.meters /1000 )/35 * config.driverRate }.sum().let { priceBusDriver.addSample(it) }
        priceService.addSample((currentTime() - const.WarmUpTime) / 60 / 60 * config.serviceRata * config.employees)
        airCarRentalAgent.queue.lengthStatistic().mean().let(acrQueueLength::addSample)
        clear()
    }

    override fun simulationFinished() {
        super.simulationFinished()

    }

    override fun clear() = listOf<Clearable>(boss, surrounding, terminal, minibus, airCarRentalAgent)
        .forEach(Clearable::clear)

    fun start(simEndTime: Double = 4.5 * 3600) {
        maxSimTime = simEndTime + const.WarmUpTime
        simulate(config.replicationCount, maxSimTime  * 1.05)
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
        minibuses = minibus.minibuses,

        employeeOccupancy = airCarRentalAgent.employees.sumByDouble(Employee::occupancy).div(config.employees),

        busOccupancy = minibus.minibuses.sumByDouble(Minibus::occupancy)//.div(config.minibuses)
    )

    val allResults = listOf(
        timeInSystemIncoming,
        timeInSystemLeaving,
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
        // acrQueueLength,
        employeeOccupancy,
        busOccupancy,
        busKiloneters,
        priceKilometers,
        priceBusDriver,
        priceService
    )

    private fun spacer(name: String = "") = Result(name, ResultType.Spacer)
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