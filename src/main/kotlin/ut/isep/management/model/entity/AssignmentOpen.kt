package ut.isep.management.model.entity

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("OPEN")
class AssignmentOpen(
    id: Long = 0,
    description: String? = null,
    availablePoints: Int? = null,
    availableSeconds: Long? = null,
    val referenceAnswer: String? = null,
) : Assignment(id, description, availablePoints, availableSeconds)