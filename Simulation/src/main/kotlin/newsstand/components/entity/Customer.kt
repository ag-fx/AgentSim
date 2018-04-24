package newsstand.components.entity

var customerId = 0
data class Customer(val arrivedToSystem: Double,val building: Building,val id:Int = customerId++)
