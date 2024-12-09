package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_MULTIPLE_CHOICE")
open class SolvedAssignmentMultipleChoice(
    id: SolvedAssignmentId = SolvedAssignmentId(),
    invite: Invite? = null,
    assignment: Assignment? = null,
    @ElementCollection
    open var userOptionsMarkedCorrect: List<Int> = listOf(),
) : SolvedAssignment(id, invite, assignment)