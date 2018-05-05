package newsstand.components.entity

import OSPABA.Simulation
import OSPDataStruct.SimQueue
import OSPStat.Stat
import OSPStat.WStat
import newsstand.clearStat
import newsstand.constants.Clearable

enum class Occupied { Yes }
data class Minibus(
    val id: Int,
    var source: Building,
    var destination: Building,
    var leftAt: Double,
    val averageSpeed: Double = 35000.0 / 3600.0,
    val type : BusType,
    var isInDestination: Boolean = false,
    val queue: SimQueue<Group> = SimQueue(),
    private val occupancy: Stat = Stat(),
    var kilometers : Double = .0
) : Clearable {
    fun secondsToDestination() = source.secondsToNext(this)

    val capacity
        get() = when (type) {
            BusType.A -> 12
            BusType.B -> 18
            BusType.C -> 30
        }

    fun addOccupancyStat() = occupancy.addSample(queue.toList().map { it.everyone().size }.fold(.0){acc, list -> acc+list }/12)
    fun occupancy() = occupancy.mean()*100

    fun clearStats(){
        occupancy.clear()
        kilometers = .0
    }


    override fun clear() {
        source          = Building.AirCarRental
        destination     = Building.TerminalOne
        leftAt          = .0//const.StartTime
        isInDestination = false
        clearStats()
    }

    fun isNotEmpty() = queue.isNotEmpty()

    fun freeSeats() = capacity - queue.size

    fun containsCustomersFromAcr() = queue.map { it.leader.building }.any { it == Building.AirCarRental }

    fun distanceFromSource(currentSimTime: Double): Double {
        return if (!isInDestination)
            Math.abs((currentSimTime - leftAt) * averageSpeed)
        else 0.0
    }

    fun distanceFromDestination(currentSimTime: Double) :Double {
        return if(!isInDestination)
            source.distanceToNext(this) - distanceFromSource(currentSimTime)
        else 0.0
    }
}


enum class BusType { A, B, C }