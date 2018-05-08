package application.view

import application.controller.MyController
import application.model.ResultModel
import javafx.scene.control.SelectionMode
import javafx.scene.layout.Priority
import tornadofx.*
import javafx.scene.input.Clipboard.getSystemClipboard
import javafx.scene.input.ClipboardContent
import javafx.beans.property.StringProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.DoubleProperty
import javafx.scene.control.TablePosition
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.scene.input.Clipboard


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
            try{
            tableview(controller.stats) {
                vgrow = Priority.ALWAYS
                columnResizePolicy = SmartResize.POLICY
                column("Štatistika", ResultModel::name)
                column("Priemer", ResultModel::mean)
                column("90% interval spoľahlivosti", ResultModel::confidence90)
                column("95% interval spoľahlivosti", ResultModel::confidence95)
            }} catch (e:Throwable){
                println(e)
            }
        }
    }

}