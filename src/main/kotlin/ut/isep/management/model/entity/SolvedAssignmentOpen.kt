package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_OPEN")
open class SolvedAssignmentOpen(
    id: SolvedAssignmentId = SolvedAssignmentId(),
    invite: Invite? = null,
    assignment: Assignment? = null,
    open var userSolution: String? = null,
) : SolvedAssignment(id, invite, assignment)
