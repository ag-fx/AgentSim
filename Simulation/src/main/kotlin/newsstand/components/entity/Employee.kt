package newsstand.components.entity

data class Employee(val id: Int, var serving: Customer? = null, var isBusy: Boolean = false){
    fun isNotBusy() = !isBusy

}


