package newsstand.components.rental

import OSPABA.*
import OSPABA.IdList.start
import OSPRNG.TriangularRNG
import OSPRNG.UniformContinuousRNG
import abaextensions.WrongMessageCode
import abaextensions.withCode
import newsstand.components.convert
import newsstand.components.entity.Building
import newsstand.constants.id
import newsstand.constants.mc

class CustomerServiceScheduler(
    mySim: Simulation,
    parent: Agent
) : Scheduler(id.CustomerServiceSchedulerID, mySim, parent) {

    override fun processMessage(msg: MessageForm) = when (msg.code()) {
        start -> msg
            .createCopy()
            .withCode(mc.customerServed)
            .convert()
            .let {
                myAgent().queueStat.addSample(mySim().currentTime() - it.group!!.startWaitingTimeCarRental)
                hold(it.serviceTime(), it)
            }

        mc.customerServed -> msg
            .createCopy()
            .convert()
            .let {
                val customer = it.group!!.leader
                val employee = myAgent().employees.first { it.serving == customer }
                employee.done(mySim())
                assistantFinished(it)
            }

        else -> throw WrongMessageCode(msg)
    }

    //region generators for service
    private fun MessageForm.serviceTime() =
        if (this.convert().group!!.leader.building == Building.AirCarRental)
            rndOut()
        else
            rndIn()


    private fun rndIn() =
        if (rndProbabilityIn.sample() in 0.0..(637.0 / 832))
            rndInSmaller.sample()
        else
            rndInBigger.sample()

    private fun rndOut() =
        if (rndProbabilityOut.sample() in 0.0..(131.0 / 978))
            rndOutBigger.sample()
        else
            rndOutSmaller.sample()


    private val rndProbabilityIn  = UniformContinuousRNG(0.0, 1.0)
    private val rndInSmaller      = TriangularRNG(1.6 * 60, 2.06 * 60, 3.0 * 60)
    private val rndInBigger       = TriangularRNG(3.0 * 60, 4.63 * 60, 5.31 * 60)

    private val rndProbabilityOut = UniformContinuousRNG(0.0, 1.0)
    private val rndOutSmaller     = TriangularRNG(0.99 * 60, 1.15 * 60, 2.21 * 60)
    private val rndOutBigger      = TriangularRNG(2.9 * 60 , 4.3 * 60, 4.8 * 60)
    //endregion

    override fun myAgent() = super.myAgent() as AirCarRentalAgent
}