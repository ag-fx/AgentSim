package application.view

import application.controller.MyController
import application.model.CustomerModel
import application.model.TerminalModel
import javafx.beans.property.SimpleListProperty
import tornadofx.*

class TerminalView : View("Terminals") {
    private val controller: MyController by inject()
    private val selected = SimpleListProperty<CustomerModel>()
    override val root = borderpane {
        left = tableview(controller.terminals) {
            maxWidth = 500.0
            column("Terminal", TerminalModel::building).apply { isSortable = false }
            onSelectionChange { selected.set(it?.queue) }
        }
        right = tableview(selected) {
            column("Terminal", CustomerModel::building).apply { isSortable = false }
            column("Cas vstupu", CustomerModel::arrivedToSystem).apply { isSortable = false }
        }
    }
}


