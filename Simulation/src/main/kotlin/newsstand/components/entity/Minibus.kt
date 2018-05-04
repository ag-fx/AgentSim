package newsstand.components.entity

import OSPDataStruct.SimQueue
import newsstand.clearStat
import newsstand.constants.Clearable

data class Minibus(
    val id: Int,
    var source: Building,
    var destination: Building,
    var leftAt: Double,
    val averageSpeed: Double = 35000.0 / 3600.0,
    val type : BusType,
    var isInDestination: Boolean = false,
    val queue: SimQueue<Group> = SimQueue(),
    var kilometers : Double = .0
) : Clearable {
    fun secondsToDestination() = source.secondsToNext(this)

    val capacity
        get() = when (type) {
            BusType.A -> 12
            BusType.B -> 18
            BusType.C -> 30
        }

    override fun clear() {
        source          = Building.AirCarRental
        destination     = Building.TerminalOne
        leftAt          = .0//const.StartTime
        isInDestination = false
        queue.clearStat()
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