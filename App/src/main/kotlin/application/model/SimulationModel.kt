package application.model

import newsstand.NewsstandSimulation
import newsstand.Result
import tornadofx.*

class SimulationModel(simulatio:NewsstandSimulation) :  ItemViewModel<NewsstandSimulation>(simulatio) {
    val timeInSystemLeaving = bind(NewsstandSimulation::timeInSystemLeaving)
    val timeInSystemIncoming = bind(NewsstandSimulation::timeInSystemIncoming)
    val timeInSystemTotal = bind(NewsstandSimulation::timeInSystemTotal)
    val queueT1 = bind(NewsstandSimulation::queueT1)
    val timeStatQueueT1 = bind(NewsstandSimulation::timeStatQueueT1)
    val queueT2 = bind(NewsstandSimulation::queueT2)
    val timeStatQueueT2 = bind(NewsstandSimulation::timeStatQueueT2)
    val queueAcr = bind(NewsstandSimulation::queueAcr)
    val timeStatQueueAcr = bind(NewsstandSimulation::timeStatQueueAcr)
    val queueAcrToT3 = bind(NewsstandSimulation::queueAcrToT3)
    val timeStatQueueAcrToT3 = bind(NewsstandSimulation::timeStatQueueAcrToT3)
    val acrQueueLength = bind(NewsstandSimulation::acrQueueLength)
    val allResults = bind(NewsstandSimulation::allResults)

}

class ResultModel(result:Result) : tornadofx.ItemViewModel<Result>(result) {
    val name = bind(Result::name)
    val mean = bind(Result::mean)
    val confidence90 = bind(Result::confidenceInterval_90)
    val confidence95 = bind(Result::confidenceInterval_95)
}