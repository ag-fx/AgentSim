package newsstand.components.entity

data class Employee(val id: Int, var serving: Customer? = null, var isBusy: Boolean = false) {
    fun isNotBusy() = !isBusy

    fun serveCustomer(customer: Customer) {
        serving = customer
        isBusy = true
    }
    fun done(){
        serving = null
        isBusy = false
    }
}

fun List<Employee>.isOneFree() = !all { it.isBusy }

