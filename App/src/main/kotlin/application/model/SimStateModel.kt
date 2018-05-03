package application.model

import OSPDataStruct.SimQueue
import javafx.util.StringConverter
import newsstand.SimState
import newsstand.constants.const
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
    queueAcrToT3 = SimQueue(),
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

class NumberTimeConv : StringConverter<Number>() {
    override fun fromString(string: String?) = TODO()

    private val time = MyTime()
    override fun toString(`object`: Number?) = `object`?.let { it as Double }?.let {
        time.actualTime = it
        return time.toString()
    } ?: "0"


}

class DoubleTimeConv : StringConverter<Double>() {
    override fun fromString(string: String?) = TODO()

    private val time = MyTime()
    override fun toString(`object`: Double?) = `object`?.let {
        time.actualTime = `object`
        return time.toString()
    } ?: "0"


}

class MyTime {
    var actualTime: Double = 0.0
        set(value) {
            field = value
            calculateTime(value + const.FirstKnownInterval - const.WarmUpTime )
        }

    private var d: Int = 0
    private var h: Int = 0
    private var m: Int = 0
    private var s: Double = 0.toDouble()
    private val lengthOfDayHours = 24.0

    private fun calculateTime(actualTime: Double) {
        s = 0.0
        m = 0
        h = 0
        d = 0

        h = Math.floor(actualTime / 3600 % this.lengthOfDayHours).toInt()

        m = Math.floor((actualTime / 3600 % lengthOfDayHours - h) * 60).toInt()
        s = actualTime % 60

        d = Math.floor(actualTime / 3600 / this.lengthOfDayHours).toInt()

    }

    override fun toString() =
        if (d > 0) "Deň $d" + " " + h + ":" + m + ":" + String.format("%.0f", s) + " " else "" + h + ":" + m + ":" + String.format("%.0f", s) + " "


}

fun main(args: Array<String>) {
    val x = MyTime().apply { actualTime = const.FirstKnownInterval - const.WarmUpTime    }
    println(x.toString())

}