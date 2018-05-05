package application.model

import newsstand.Config
import tornadofx.*

class ConfigModel : ItemViewModel<Config> {
    constructor(item:Config) : super(item)
    constructor() : super()

    val minibuses = bind(Config::minibuses)
    val employees = bind(Config::employees)
    val busType = bind(Config::busType)
    val slowDownAfterWarmUp = bind(Config::slowDownAfterWarmUp)
    val replicationCount = bind(Config::replicationCount)
    override fun onCommit() {
        super.onCommit()

        item = Config(
            minibuses = minibuses.value,
            employees = employees.value,
            busType = busType.value,
            slowDownAfterWarmUp = slowDownAfterWarmUp.value,
            replicationCount = replicationCount.value
        )
    }
}