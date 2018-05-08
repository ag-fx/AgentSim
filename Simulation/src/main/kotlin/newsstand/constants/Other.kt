package newsstand.constants

object const {
    const val FirstKnownInterval = 60 * 60 * 16.0
    const val WarmUpTime  = 60 * 60 * 4.0
    const val ClosingDown = 60*60*20.5 + WarmUpTime

}

interface Clearable{
    fun clear()
}