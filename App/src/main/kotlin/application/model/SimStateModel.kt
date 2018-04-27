package application.model

import OSPDataStruct.SimQueue
import javafx.util.StringConverter
import newsstand.SimState
import tornadofx.*
import java.text.DecimalFormat

class SimStateModel(sim: SimState = initState) : ItemViewModel<SimState>(sim) {
    val queueT1 = sim.queueT1.lengthStatistic()
    val statQueueT1 = sim.timeStatQueueT1
    val statQueueT2 = sim.timeStatQueueT2
    val queueT2 = sim.queueT2.lengthStatistic()
    val queueAcr = sim.queueAcr.lengthStatistic()
}

val initState = SimState(
    queueT1 = SimQueue(),
    queueT2 = SimQueue(),
    queueAcr = SimQueue(),
    acrEmployees = emptyList(),
    minibuses = emptyList()

)

val format = DecimalFormat("#.##")
fun Double.format() = format.format(this)

class XSim(val f: (SimStateModel) -> String) : StringConverter<SimStateModel>() {
    override fun toString(`object`: SimStateModel?) = `object`?.let { f(`object`) } ?: "N/A"

    override fun fromString(string: String?) = TODO()

}

class DoubleConv(val f: (Double) -> String) : StringConverter<Number>() {
    override fun toString(`object`: Number?) = `object`?.let { f(`object` as Double) } ?: "N/A"

    override fun fromString(string: String?): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}