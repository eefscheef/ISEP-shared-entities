package ut.isep.management.model.entity

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolvedAssignmentCodingUnitTest {

    @Test
    fun `test scoredPoints returns 0 when not all test results pass`() {
        val assignment = mockk<Assignment> {
            every { availablePoints } returns 100
        }

        val solvedAssignment = SolvedAssignmentCoding(assignment = assignment)
        solvedAssignment.testResults.addAll(
            listOf(
                TestResult(passed = true),
                TestResult(passed = false),
                TestResult(passed = true)
            )
        )
        solvedAssignment.secretTestResults.addAll(
            listOf(
                TestResult(passed = true),
                TestResult(passed = true)
            )
        )

        assertThat(solvedAssignment.scoredPoints).isEqualTo(0)
    }

    @Test
    fun `test scoredPoints calculates percentage correctly`() {
        val assignment = mockk<Assignment> {
            every { availablePoints } returns 100
        }

        val solvedAssignment = SolvedAssignmentCoding(assignment = assignment)
        solvedAssignment.testResults.addAll(
            listOf(
                TestResult(passed = true),
                TestResult(passed = true),
                TestResult(passed = true)
            )
        )
        solvedAssignment.secretTestResults.addAll(
            listOf(
                TestResult(passed = true),
                TestResult(passed = false)
            )
        )

        assertThat(solvedAssignment.scoredPoints).isEqualTo(50)
    }

    @Test
    fun `test addTestResults correctly assigns solvedAssignmentCoding and adds results`() {
        val solvedAssignment = SolvedAssignmentCoding()
        val testResults = listOf(TestResult(passed = true), TestResult(passed = false))

        solvedAssignment.addTestResults(testResults)

        assertThat(solvedAssignment.testResults).containsExactlyElementsOf(testResults)
        assertThat(testResults.all { it.solvedAssignmentCoding == solvedAssignment }).isTrue()
    }

    @Test
    fun `test addSecretTestResults correctly assigns solvedAssignmentCoding and adds results`() {
        val solvedAssignment = SolvedAssignmentCoding()
        val secretTestResults = listOf(TestResult(passed = true), TestResult(passed = true))

        solvedAssignment.addSecretTestResults(secretTestResults)

        assertThat(solvedAssignment.secretTestResults).containsExactlyElementsOf(secretTestResults)
        assertThat(secretTestResults.all { it.solvedAssignmentCoding == solvedAssignment }).isTrue()
    }
}
