package newsstand.components.entity

import OSPDataStruct.SimQueue

data class Minibus(
    val id: Int,
    var source: Building,
    var destination: Building,
    var leftAt: Double,
    val averageSpeed: Double = 35000.0 / 3600.0,
    val capacity: Int = 12,
    var isInDestination: Boolean = false,
    val queue: SimQueue<Group> = SimQueue()
) {
    fun secondsToDestination() = source.secondsToNext(averageSpeed)
    fun isNotFull() = queue.size < capacity
    fun isNotEmpty() = queue.isNotEmpty()
    fun freeSeats() = capacity - queue.size

    fun distanceFromSource(currentSimTime: Double): Double {
        return if (!isInDestination)
            Math.abs((currentSimTime - leftAt) * averageSpeed)
        else 0.0
    }

    fun distanceFromDestination(currentSimTime: Double) :Double {
        return if(!isInDestination)
            source.distanceToNext() - distanceFromSource(currentSimTime)
        else 0.0
    }
}

