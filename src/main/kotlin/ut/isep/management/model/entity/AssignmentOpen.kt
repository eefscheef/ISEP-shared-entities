package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("OPEN")
class AssignmentOpen(
    id: Long = 0,
    description: String? = null,
    availablePoints: Int? = null,
    val referenceAnswer: String? = null,
) : Assignment(id, description, availablePoints)