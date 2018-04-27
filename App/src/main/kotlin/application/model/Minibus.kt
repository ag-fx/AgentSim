package application.model

import newsstand.components.entity.Minibus
import tornadofx.*

class MinibusModel(val simTime:Double,minibus: Minibus) : tornadofx.ItemViewModel<Minibus>(minibus) {
    val id = bind(Minibus::id)
    val source = bind(Minibus::source)
    val destination = bind(Minibus::destination)
    val leftAt = bind(Minibus::leftAt)
    val averageSpeed = bind(Minibus::averageSpeed)
    val capacity = bind(Minibus::capacity)
    val distanceToDestination = minibus.distanceFromDestination(simTime).toProperty()
    val isInDestination = bind(Minibus::isInDestination)
    val queue = minibus.queue.toList().observable()
}


