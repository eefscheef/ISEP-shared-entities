package ut.isep.management.model.entity

import enumerable.InviteStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class InviteUnitTest {

    private val applicant = mockk<Applicant>(relaxed = true)
    private val assessment = mockk<Assessment>(relaxed = true) {
        every { sections } returns mutableListOf(
            mockk {
                every { assignments } returns mutableListOf(
                    mockk {
                        every { id } returns 1L
                        every { assignmentType } returns AssignmentType.OPEN
                    },
                    mockk {
                        every { id } returns 2L
                        every { assignmentType } returns AssignmentType.CODING
                    },
                    mockk {
                        every { id } returns 3L
                        every { assignmentType } returns AssignmentType.MULTIPLE_CHOICE
                    }
                )
            }
        )
    }

    @Test
    fun `test createInvite() should initialize solutions and measuredSecondsPerSection`() {
        val invite = Invite.createInvite(applicant, assessment)

        assertThat(invite.solutions).isNotEmpty
        assertThat(invite.measuredSecondsPerSection).isNotEmpty
        assertThat(invite.status).isEqualTo(InviteStatus.not_started)

        verify { assessment.sections }
    }

    @Test
    fun `test initializeSolutions() should create SolvedAssignments`() {
        val invite = Invite(applicant = applicant, assessment = assessment)
        invite.initializeSolutions()

        assertThat(invite.solutions).hasSize(3)
        assertThat(invite.solutions.map { it.assignment!!.id }).containsExactlyInAnyOrder(1L, 2L, 3L)
    }

    @Test
    fun `test initializeMeasuredSecondsPerSection() should create TimingPerSection`() {
        val invite = Invite(applicant = applicant, assessment = assessment)
        invite.initializeMeasuredSecondsPerSection()

        assertThat(invite.measuredSecondsPerSection).hasSize(1)
    }
}