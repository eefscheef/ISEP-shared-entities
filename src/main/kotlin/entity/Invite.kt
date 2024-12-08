package ut.isep.management.model.entity

import entity.*
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity
open class Invite(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override val id: UUID = UUID.randomUUID(),

    @OneToOne
    @JoinColumn(name = "applicant_id")
    open var applicant: Applicant = Applicant(),

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    open var assessment: Assessment = Assessment(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "solution_id")
    open var solutions: MutableList<SolvedAssignment> = emptySolvedAssignments(assessment),

    open var invitedAt: ZonedDateTime = ZonedDateTime.now()
) : BaseEntity<UUID>

private fun emptySolvedAssignments(assessment: Assessment): MutableList<SolvedAssignment> {
    return assessment.sections.flatMap { section ->
        section.assignments.map(::createSolvedAssignment)
    }.toMutableList()
}

private fun createSolvedAssignment(assignment: Assignment): SolvedAssignment {
    return when (assignment) {
        is AssignmentOpen -> SolvedAssignmentOpen(assignment = assignment)
        is AssignmentCoding -> SolvedAssignmentCoding(assignment = assignment)
        is AssignmentMultipleChoice -> SolvedAssignmentMultipleChoice(assignment = assignment)
        else -> throw UnsupportedOperationException("Unsupported assignment type: ${assignment::class}")
    }
}
