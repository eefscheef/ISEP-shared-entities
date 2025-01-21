package ut.isep.management.model.entity

import enumerable.InviteStatus
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Entity
open class Invite(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    open var applicant: Applicant? = null,

    @ManyToOne
    @JoinColumns(
        JoinColumn(name = "assessment_tag", referencedColumnName = "tag", nullable = false),
        JoinColumn(name = "assessment_gitCommitHash", referencedColumnName = "gitCommitHash", nullable = false)
    )
    open var assessment: Assessment? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "invite")
    open var solutions: MutableList<SolvedAssignment> = mutableListOf(),

    open val invitedAt: OffsetDateTime = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC),
    open var expiresAt: OffsetDateTime = invitedAt.plusWeeks(1).withHour(23).withMinute(59).withSecond(59).withNano(0),
    open var assessmentStartedAt: OffsetDateTime? = null,
    open var assessmentFinishedAt: OffsetDateTime? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "invite")
    open var measuredSecondsPerSection: MutableList<TimingPerSection> = mutableListOf(),

    open var status: InviteStatus = InviteStatus.not_started,

    ) : BaseEntity<UUID> {
    fun initializeSolutions() {
        solutions = assessment!!.sections.flatMap { section ->
            section.assignments.map { createSolvedAssignment(it, this) }
        }.toMutableList()
    }

    fun initializeMeasuredSecondsPerSection() {
        measuredSecondsPerSection = assessment!!.sections.map { section ->
            createMeasuredTimeSection(section, this)
        }.toMutableList()
    }

    companion object {
        fun createInvite(applicant: Applicant, assessment: Assessment, expiresAt: OffsetDateTime? = null): Invite {
            val invite: Invite = if (expiresAt != null) {
                Invite(applicant = applicant, assessment = assessment, expiresAt = expiresAt)
            } else {
                Invite(applicant = applicant, assessment = assessment)
            }
            invite.initializeSolutions()
            invite.initializeMeasuredSecondsPerSection()
            return invite
        }
    }
}

private fun createSolvedAssignment(assignment: Assignment, invite: Invite): SolvedAssignment {
    return when (assignment.assignmentType) {
        AssignmentType.OPEN -> SolvedAssignmentOpen(assignment = assignment, invite = invite)
        AssignmentType.CODING -> SolvedAssignmentCoding(assignment = assignment, invite = invite)
        AssignmentType.MULTIPLE_CHOICE -> SolvedAssignmentMultipleChoice(assignment = assignment, invite = invite)
        else -> throw UnsupportedOperationException("Unsupported assignment type: ${assignment::class}")
    }
}

private fun createMeasuredTimeSection(section: Section, invite: Invite): TimingPerSection {
    return TimingPerSection(section = section, invite = invite)
}
