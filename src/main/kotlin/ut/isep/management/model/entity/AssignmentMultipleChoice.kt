package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
class AssignmentMultipleChoice(
    id: Long = 0,
    description: String? = null,
    @ElementCollection
    val optionToSolution: Map<String, Boolean> = mapOf(),
) : Assignment(id, description)