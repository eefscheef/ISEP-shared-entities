package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("OPEN")
class AssignmentOpen(
    id: Long = 0,
    description: String = "",
    val referenceSolution: String = "",
) : Assignment(id, description)