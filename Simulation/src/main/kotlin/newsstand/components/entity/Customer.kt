package newsstand.components.entity

import OSPDataStruct.SimQueue

data class Customer(
    val arrivedToSystem: Double,
    val building: Building
){
    override fun toString() = arrivedToSystem.toString()
}
