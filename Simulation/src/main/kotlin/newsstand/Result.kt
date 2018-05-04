package newsstand

import OSPStat.Stat
import java.text.DecimalFormat

enum class ResultType { Other, Time, Spacer }

data class Result(val name: String, private val resultType: ResultType = ResultType.Other, private val stat: Stat = Stat()) {
    private val format = DecimalFormat("#.####")
    private fun Double.format() = format.format(this)

    fun addSample(sample: Double) = stat.addSample(sample)

    fun mean() = when (resultType) {
        ResultType.Other -> stat.mean().format()
        ResultType.Time  -> (stat.mean() / 60.0).format()
        ResultType.Spacer -> ""
    }

    fun confidenceInterval_90(): String {
        if(resultType==ResultType.Spacer)  return ""
        if (stat.sampleSize() > 2.1) {
            val c = when (resultType) {
                ResultType.Other -> stat.confidenceInterval_90().map { it.format() }
                ResultType.Time  -> stat.confidenceInterval_90().map { it / 60.0 }.map { it.format() }
                ResultType.Spacer -> TODO()
            }
            return "<${c[0]} , ${c[1]}>"
        } else return "NaN"

    }

    fun confidenceInterval_95(): String {
        if(resultType==ResultType.Spacer)  return ""
        if (stat.sampleSize() > 2.1) {
            val c = when (resultType) {
                ResultType.Other -> stat.confidenceInterval_95().map { it.format() }
                ResultType.Time  -> stat.confidenceInterval_95().map { it / 60.0 }.map { it.format() }
                ResultType.Spacer -> TODO()
            }
            return "<${c[0]} , ${c[1]}>"
        } else return "NaN"
    }
}