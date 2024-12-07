package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
class AssignmentMultipleChoice(
    id: Long = 0,
    description: String = "",
    @ElementCollection
    val options: List<String> = listOf(),
    var isMultipleAnswers: Boolean = false,
) : Assignment(id, description)