package application.view

import application.controller.MyController
import application.model.ConfigModel
import javafx.geometry.Insets
import javafx.scene.control.TextField
import newsstand.Config
import newsstand.components.entity.BusType
import tornadofx.*


class ConfigView : View("Simulácia") {

    override val root = borderpane {
        padding = Insets(20.0)

        left(ConfigCardView::class)

        center(StatisticsView::class)

    }


}


fun TextField.intValidator() = validator {
    if (!it.isInt()) error("Must be int") else null
}

fun TextField.intTyper() = textProperty().onChange { if (it?.matches(Regex("\\d*")) ?: false) text else text = null }


fun String?.isInt(): Boolean {
    try {
        this?.toInt() ?: return false
        return true
    } catch (e: Exception) {
        return false
    }
}