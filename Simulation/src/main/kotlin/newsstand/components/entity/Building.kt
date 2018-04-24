package newsstand.components.entity

enum class Building{
    TerminalOne,
    TerminalTwo,
    AirCarRental
}

fun Building.distanceToNext() = when (this) {
    Building.TerminalOne -> 500.0
    Building.TerminalTwo -> 2500.0
    Building.AirCarRental -> 6400.0
}

fun Building.nextStop() = when (this) {
    Building.TerminalOne  -> Building.TerminalTwo
    Building.TerminalTwo  -> Building.AirCarRental
    Building.AirCarRental -> Building.TerminalOne
}

fun Building.secondsToNext(speed:Double) = when (this) {
    Building.TerminalOne -> 500.0 / speed
    Building.TerminalTwo -> 2500.0 / speed
    Building.AirCarRental -> 6400.0 / speed
}
