package application.view

import application.controller.MyController
import application.model.ResultModel
import javafx.scene.layout.Priority
import tornadofx.*

class StatisticsView : View("Config") {
    private val controller: MyController by inject()

    override val root = vbox {
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        paddingLeft = 12.0
        vbox {
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            addClass("card")
            text("Stats").addClass("card-title")
            tableview(controller.stats) {
                vgrow = Priority.ALWAYS
                columnResizePolicy = SmartResize.POLICY
                column("Štatistika", ResultModel::name) { isSortable = false }
                column("Priemer", ResultModel::mean) { isSortable = false }
                column("90% interval spoľahlivosti", ResultModel::confidence90) { isSortable = false }
                column("95% interval spoľahlivosti", ResultModel::confidence95) { isSortable = false }
            }
        }
    }

}