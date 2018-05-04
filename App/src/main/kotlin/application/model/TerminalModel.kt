package application.model

import newsstand.components.entity.Terminal
import tornadofx.*

class TerminalModel(terminal: Terminal) : ItemViewModel<Terminal>(terminal) {
    val building = bind(Terminal::building)
    val queue    = terminal.queue.toList().map(::CustomerModel).observable()
    val avgQueueLength = terminal.queue.lengthStatistic().mean()
    val timeInQueue = terminal.timeInQueueStat
    override fun equals(other: Any?) = other is TerminalModel && building.value == other.building.value
}

