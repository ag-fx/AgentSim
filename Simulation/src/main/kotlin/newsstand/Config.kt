package newsstand

import newsstand.components.entity.BusType

data class Config(
    val minibuses: Int = 3,
    val employees: Int = 3,
    val xtra :Int = -1,
    val busType: BusType = BusType.A,
    val driverRate :Double = 12.5,
    val serviceRata:Double = 11.5,
    var slowDownAfterWarmUp: Boolean = true,
    val replicationCount: Int = 1000
)