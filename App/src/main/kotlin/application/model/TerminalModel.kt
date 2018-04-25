package application.model

import newsstand.components.entity.Terminal
import tornadofx.*

class TerminalModel(terminal: Terminal) : ItemViewModel<Terminal>(terminal) {
    val building = bind(Terminal::building)
    val queue    = terminal.queue.toList().map(::CustomerModel).observable()
}


