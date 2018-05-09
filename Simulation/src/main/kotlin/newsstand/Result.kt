package newsstand

import OSPStat.Stat
import newsstand.ResultType.*
import java.text.DecimalFormat

enum class ResultType { Other, Time, Spacer }

data class Result(val name: String, private val resultType: ResultType = Other, private val stat: Stat = Stat()) {
    private val format = DecimalFormat("#.####")
    private fun Double.format() = format.format(this)

    fun addSample(sample: Double) = stat.addSample(sample)

    fun mean() = when (resultType) {
        Other  -> stat.mean().format()
        Time   -> (stat.mean() / 60.0).format()
        Spacer -> ""
    }

    fun confidenceInterval90(): String {
        if (resultType == Spacer) return ""
        if (stat.sampleSize() > 2.1) {
            val c = when (resultType) {
                Other  -> stat.confidenceInterval_90().map { it.format() }
                Time   -> stat.confidenceInterval_90().map { it / 60.0 }.map { it.format() }
                Spacer -> TODO()
            }
            return "<${c[0]} , ${c[1]}>"
        } else return "NaN"

    }

    fun confidenceInterval95(): String {
        if (resultType == Spacer) return ""
        if (stat.sampleSize() > 2.1) {
            val c = when (resultType) {
                Other  -> stat.confidenceInterval_95().map { it.format() }
                Time   -> stat.confidenceInterval_95().map { it / 60.0 }.map { it.format() }
                Spacer -> TODO()
            }
            return "<${c[0]} , ${c[1]}>"
        } else return "NaN"
    }

    fun toExcel() = when (resultType) {
        Other  -> println(name + "\t" + stat.mean() + "\t" + stat.confidenceInterval_90()[0] + "\t" + stat.confidenceInterval_90()[1])
        Time   -> println(name + "\t" + stat.mean()/60 + "\t" + stat.confidenceInterval_90()[0]/60 + "\t" + stat.confidenceInterval_90()[1]/60)
        Spacer -> print("")
    }

}