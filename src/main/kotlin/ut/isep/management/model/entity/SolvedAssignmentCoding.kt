package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_CODING")
open class SolvedAssignmentCoding(
    id: SolvedAssignmentId = SolvedAssignmentId(),
    invite: Invite? = null,
    assignment: Assignment? = null,

    @Column(columnDefinition = "text")
    open var userCode: String = "",

    @Column(columnDefinition = "text")
    open var testCode: String = "",

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "solvedAssignmentCoding")
    open var testResults: MutableList<TestResult> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "solvedAssignmentCoding")
    open var secretTestResults: MutableList<TestResult> = mutableListOf(),
) : SolvedAssignment(id, invite, assignment)
