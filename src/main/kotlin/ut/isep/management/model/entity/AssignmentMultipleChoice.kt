package ut.isep.management.model.entity

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
class AssignmentMultipleChoice(
    id: Long = 0,
    description: String? = null,
    availablePoints: Int? = null,
    availableSeconds: Long? = null,
    @ElementCollection
    val optionToSolution: Map<String, Boolean> = mapOf(),
) : Assignment(id, description, availablePoints, availableSeconds)