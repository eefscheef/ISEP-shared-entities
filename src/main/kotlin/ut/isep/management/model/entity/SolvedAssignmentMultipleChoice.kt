package ut.isep.management.model.entity

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SOLVED_MULTIPLE_CHOICE")
open class SolvedAssignmentMultipleChoice(
    id: SolvedAssignmentId = SolvedAssignmentId(),
    invite: Invite? = null,
    assignment: Assignment? = null,
    @ElementCollection
    open var userOptionsMarkedCorrect: List<String> = listOf(),
) : SolvedAssignment(id, invite, assignment)