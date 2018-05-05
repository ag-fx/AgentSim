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
    private val occupancy: SimQueue<Work> = SimQueue(),
    private var serveStart: Double = 0.0,
    var workTime: Double = 0.0
) : Clearable {


    fun setStat(mySym: Simulation) {
        occupancy.setLengthStatistic(WStat(mySym))
    }

    fun occupancy() = occupancy.lengthStatistic().mean() * 100

    override fun clear() {
        serveStart = 0.0
        if (occupancy.isNotEmpty()) occupancy.remove()  // beacuse of some bug at the start.
        serving = null
        workTime = 0.0
        serveStart = 0.0
        occupancy.clearStat()
    }

    fun clearWorkTime(){
        workTime = 0.0
    }

    fun isNotBusy() = !isBusy

    fun serveCustomer(customer: Customer, mySym: Simulation) {
        serving = customer
        isBusy = true
        serveStart = mySym.currentTime()
        occupancy.add(Work.Busy)
    }

    fun done(mySym: Simulation) {
        workTime += mySym.currentTime() - serveStart
        serveStart = 0.0
        if (occupancy.isNotEmpty()) occupancy.remove()
        serving = null
        isBusy = false
    }

}

fun List<Employee>.isOneFree() = any(Employee::isNotBusy)

enum class Work { Busy }