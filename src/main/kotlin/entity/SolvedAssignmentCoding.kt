package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_CODING")
open class SolvedAssignmentCoding(
    id: Long = 0,
    assignment: AssignmentCoding? = null,
    open var userCode: String = "",
) : SolvedAssignment(id, assignment)
