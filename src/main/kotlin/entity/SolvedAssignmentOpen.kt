package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_OPEN")
open class SolvedAssignmentOpen(
    id: Long = 0,
    assignment: AssignmentOpen? = null,
    open var userSolution: String = "",
) : SolvedAssignment(id, assignment)
