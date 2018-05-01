package newsstand.components.entity

data class Customer(
    val arrivedToSystem: Double,
    val building: Building
) {
    override fun toString() = arrivedToSystem.toString()
}

data class Group(
    val leader : Customer,
    val family : List<Customer>
)

sealed class CustomerType<out First, out Value> {
    data class One<out A> (val value: A): CustomerType<A, Nothing>()
    data class Group<out A> (val value: A): CustomerType<Nothing, A>()
}