package newsstand.components.entity

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.WStat
import newsstand.clearStat
import newsstand.constants.Clearable

data class Employee(
    val id: Int,
    var serving: Customer? = null,
    var isBusy: Boolean = false,
    val stat: SimQueue<Work> = SimQueue(),
    private var serveStart: Double = 0.0,
    var workTime: Double = 0.0
) : Clearable {


    fun setStat(mySym: Simulation) {
        stat.setLengthStatistic(WStat(mySym))
    }

    override fun clear() {
        serveStart = 0.0
        if((stat.isNotEmpty())) stat.remove()
        serving = null
        workTime = 0.0
        serveStart = 0.0
        stat.clearStat()
    }

    fun isNotBusy() = !isBusy

    fun serveCustomer(customer: Customer, mySym: Simulation) {
        serving = customer
        isBusy = true
        serveStart = mySym.currentTime()
        stat.add(Work.Busy)
    }

    fun done(mySym: Simulation) {
        workTime += mySym.currentTime() - serveStart
        serveStart = 0.0
        stat.remove()
        serving = null
        isBusy = false
    }

}

fun List<Employee>.isOneFree() = any(Employee::isNotBusy)

enum class Work { Busy }