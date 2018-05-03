package newsstand

import OSPDataStruct.SimQueue
import OSPStat.Stat
import newsstand.components.entity.*

data class SimState(
    val queueT1: SimQueue<Customer>,
    val queueT2: SimQueue<Customer>,
    val queueAcr: SimQueue<Group>,
    val queueAcrToT3: SimQueue<Customer>,
    val acrEmployees: List<Employee>,
    val minibuses: List<Minibus>,
    val timeStatQueueT1: Stat = Stat(),
    val timeStatQueueT2: Stat = Stat()
)



