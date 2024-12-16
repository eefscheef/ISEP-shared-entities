package ut.isep.management.model.entity

import enumerable.InviteStatus
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
open class Invite(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    open var applicant: Applicant = Applicant(),

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    open var assessment: Assessment = Assessment(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "invite_id")
    open var solutions: MutableList<SolvedAssignment> = mutableListOf(),

    open val invitedAt: OffsetDateTime = OffsetDateTime.now(),
    open var expiresAt: OffsetDateTime = invitedAt.plusWeeks(1),

    open var status: InviteStatus = InviteStatus.not_started,

    ) : BaseEntity<UUID> {
    fun initializeSolutions() {
        solutions = assessment.sections.flatMap { section ->
            section.assignments.map { createSolvedAssignment(it, this) }
        }.toMutableList()
    }

    companion object {
        fun createInvite(applicant: Applicant, assessment: Assessment): Invite {
            val invite = Invite(applicant = applicant, assessment = assessment)
            invite.initializeSolutions()
            return invite
        }
    }
}

private fun createSolvedAssignment(assignment: Assignment, invite: Invite): SolvedAssignment {
    return when (assignment) {
        is AssignmentOpen -> SolvedAssignmentOpen(assignment = assignment, invite = invite)
        is AssignmentCoding -> SolvedAssignmentCoding(assignment = assignment, invite = invite)
        is AssignmentMultipleChoice -> SolvedAssignmentMultipleChoice(assignment = assignment, invite = invite)
        else -> throw UnsupportedOperationException("Unsupported assignment type: ${assignment::class}")
    }
}
