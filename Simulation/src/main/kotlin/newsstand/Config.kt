package newsstand

import newsstand.components.entity.BusType

data class Config(
    val minibuses: Int = 3,
    val employees: Int = 3,
    val busType: BusType = BusType.A,
    var slowDownAfterWarmUp: Boolean = false,
    val replicationCount: Int = 1000
)