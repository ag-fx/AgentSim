package newsstand.constants

object mc {
    const val init = 100

    const val newCustomer = 200
    const val customerArrivalTerminalOne = 230
    const val customerArrivalTerminalTwo = 240

    const val minibusGoTo = 500

    const val minibusArrivedToDestination = 501
    const val terminalOneMinibusArrival = 502
    const val customerEnteredBus = 503
    const val customerExitedMinibus = 504
    const val customerEnteredMinibus = 505

    const val enterMinibusRequest = 400
    const val enterMinibusResponse = 401

    const val terminalTwoMinibusArrival = 601

    const val airCarRentalMinibusArrival = 700
     const val customerServed = 702
    const val customerService = 703
    const val getCustomerFromBusRequest = 704
    const val getCustomerFromBusResponse = 705
    const val moveCustomerToQueueAtAirCarRental = 706

    const val customerLeaving = 9999
}