package application.view

import application.controller.MyController
import application.model.*
import javafx.beans.property.SimpleListProperty
import javafx.geometry.Insets
import javafx.scene.layout.Priority
import tornadofx.*

class MinibusesView : View("Minibusy") {
    private val controller: MyController by inject()
    private val selected = SimpleListProperty<CustomerModel>()
    override val root = borderpane {
        padding = Insets(20.0)

        center = vbox {
            addClass("card")
            text("Minibuses").addClass("card-title")

            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            tableview(controller.minibuses) {

                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
                smartResize()
                column("ID", MinibusModel::id).apply { isSortable = false }
                column("Odkial", MinibusModel::source).apply { isSortable = false }
                column("Kam", MinibusModel::destination).apply { isSortable = false }
                column("Odisiel", MinibusModel::leftAt) {
                    isSortable = false
                    converter(DoubleTimeConv())
                }
                column("vzdialenost do ciela", MinibusModel::distanceToDestination) {
                    converter(DoubleConv { it.format() })
                }
                column("Je v zastavke", MinibusModel::isInDestination).apply { isSortable = false }.remainingWidth()
                onSelectionChange {
                    selected.set(it?.queue?.toList()?.map { CustomerModel(it) }?.observable()
                        ?: emptyList<CustomerModel>().observable())
                }
            }
        }
        right = hbox {

            paddingLeft = 24
            vbox {
                addClass("card")
                text("Customers in selected minibus").addClass("card-title")
                tableview(selected) {

                    smartResize()

                    column("Terminal", CustomerModel::building).apply { isSortable = false }
                    column("Cas vstupu", CustomerModel::arrivedToSystem).apply { isSortable = false }
                }
            }
        }
    }
}



