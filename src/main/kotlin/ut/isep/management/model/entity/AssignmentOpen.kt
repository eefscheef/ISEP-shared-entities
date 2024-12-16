package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("OPEN")
class AssignmentOpen(
    id: Long = 0,
    description: String? = null,
    val referenceSolution: String? = null,
) : Assignment(id, description)