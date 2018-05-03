package application.model

import newsstand.components.entity.Group
import tornadofx.*

class GroupModel(group:Group) : ItemViewModel<Group>(group) {
    val leader = bind(Group::leader)
    val building = bind(Group::building)
    val arrivedToSystem = bind(Group::arrivedToSystem)
    //val building = item.leader.building.toProperty()
//    val arrivedToSystem = item.leader.arrivedToSystem.toProperty()
    val family = bind(Group::family)
}