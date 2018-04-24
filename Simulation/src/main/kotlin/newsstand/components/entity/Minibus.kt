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
    val queue: SimQueue<Customer> = SimQueue()
) {
    fun secondsToDestination() = destination.secondsToNext(averageSpeed)
    fun isNotFull() = queue.size <=capacity
}
