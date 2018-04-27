package application.view

import application.controller.MyController
import application.model.CustomerModel
import application.model.MinibusModel
import javafx.beans.property.SimpleListProperty
import tornadofx.*

class MinibusesView : View("Minibusy") {
    private val controller: MyController by inject()
    private val selected = SimpleListProperty<CustomerModel>()
    override val root = hbox {
        tableview(controller.minibuses) {
            maxWidth = 500.0
            column("ID", MinibusModel::id).apply { isSortable = false }
            column("Odkial", MinibusModel::source).apply { isSortable = false }
            column("Kam", MinibusModel::destination).apply { isSortable = false }
            column("Odisiel", MinibusModel::leftAt).apply { isSortable = false }
            column("vzdialenost do ciela", MinibusModel::distanceToDestination)
            column("Je v zastavke", MinibusModel::isInDestination).apply { isSortable = false }
            onSelectionChange {
                selected.set(it?.queue?.toList()?.map { CustomerModel(it) }?.observable()
                    ?: emptyList<CustomerModel>().observable())
            }
        }
        spacer()
        tableview(selected) {
            column("Terminal", CustomerModel::building).apply { isSortable = false }
            column("Cas vstupu", CustomerModel::arrivedToSystem).apply { isSortable = false }
        }
        spacer()

    }
}



