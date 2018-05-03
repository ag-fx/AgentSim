package newsstand.components.entity

enum class Building{
    TerminalOne,
    TerminalTwo,
    TerminalThree,
    AirCarRental
}

fun Building.distanceToNext(minibus: Minibus) = when (this) {
    Building.TerminalOne   -> 500.0
    Building.TerminalTwo   -> 3400.0
    Building.AirCarRental  -> if (minibus.containsCustomersFromAcr()) 2900.0 else 2500.0
    Building.TerminalThree -> 900.0
}

fun Building.nextStop(minibus: Minibus) = when (this) {
    Building.TerminalOne   -> Building.TerminalTwo
    Building.TerminalTwo   -> Building.AirCarRental
    Building.AirCarRental  -> if (minibus.containsCustomersFromAcr()) Building.TerminalThree else Building.TerminalOne
    Building.TerminalThree -> Building.TerminalOne
}

fun Building.secondsToNext(minibus: Minibus) = this.distanceToNext(minibus) / minibus.averageSpeed

  /*  when (this) {
    Building.TerminalOne  -> 500.0 / speed
    Building.TerminalTwo  -> 2500.0 / speed
    Building.AirCarRental -> 6400.0 / speed
 //   Building.TerminalThree -> TODO()
}*/
