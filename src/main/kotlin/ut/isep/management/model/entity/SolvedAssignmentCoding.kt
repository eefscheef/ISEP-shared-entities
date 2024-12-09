package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_CODING")
open class SolvedAssignmentCoding(
    id: SolvedAssignmentId = SolvedAssignmentId(),
    invite: Invite? = null,
    assignment: Assignment? = null,
    open var userCode: String = "",
) : SolvedAssignment(id, invite, assignment)
