package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_MULTIPLE_CHOICE")
open class SolvedAssignmentMultipleChoice(
    id: Long = 0,
    assignment: AssignmentMultipleChoice? = null,
    @ElementCollection
    open var userOptionsMarkedCorrect: List<Int> = listOf(),
) : SolvedAssignment(id, assignment)
