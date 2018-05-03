package newsstand.constants

object const {
    const val StartTime = 60 * 60 * 15.9
    const val FirstKnownInterval = 60 * 60 * 16.0
    const val WarmUpTime = 60 * 60 * 4.0
}

interface Clearable{
    fun clear()
}