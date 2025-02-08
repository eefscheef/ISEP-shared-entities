package ut.isep.management.model.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SectionUnitTest {
    private lateinit var section: Section
    private lateinit var assignment1: Assignment
    private lateinit var assignment2: Assignment

    @BeforeEach
    fun setUp() {
        section = Section(title = "Math Section")

        assignment1 = Assignment(id = 1L, availableSeconds = 100, availablePoints = 10)
        assignment2 = Assignment(id = 2L, availableSeconds = 200, availablePoints = 20)

        section.assignments.add(assignment1)
        section.assignments.add(assignment2)
    }

    @Test
    fun `test availableSeconds should return sum of assignment seconds`() {
        assertThat(section.availableSeconds).isEqualTo(300L)
    }

    @Test
    fun `test availablePoints should return sum of assignment points`() {
        assertThat(section.availablePoints).isEqualTo(30)
    }

    @Test
    fun `test addAssignment should add a new assignment`() {
        val newAssignment = Assignment(id = 3L, availableSeconds = 150, availablePoints = 15)
        section.addAssignment(newAssignment)

        assertThat(section.assignments).contains(newAssignment)
        assertThat(section.availableSeconds).isEqualTo(450L)
        assertThat(section.availablePoints).isEqualTo(45)
    }

    @Test
    fun `test addAssignment should throw exception when adding duplicate assignment`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            section.addAssignment(assignment1)
        }
        assertThat(exception.message).contains("Assignment with ID 1 is already present in the section.")
    }

    @Test
    fun `test removeAssignmentById should remove the correct assignment`() {
        val isRemoved = section.removeAssignmentById(1L)
        assertThat(isRemoved).isTrue()
        assertThat(section.assignments).doesNotContain(assignment1)
    }

    @Test
    fun `test removeAssignmentById should return false if assignment does not exist`() {
        val isRemoved = section.removeAssignmentById(99L)
        assertThat(isRemoved).isFalse()
    }

    @Test
    fun `test availableSeconds should throw exception when any assignment has null seconds`() {
        val assignmentWithNullSeconds = Assignment(id = 4L, availableSeconds = null)
        section.assignments.add(assignmentWithNullSeconds)

        val exception = assertThrows(IllegalStateException::class.java) {
            section.availableSeconds
        }
        assertThat(exception.message).contains("Some assignments of this section have null availableSeconds")
    }

    @Test
    fun `test availablePoints should throw exception when any assignment has null points`() {
        val assignmentWithNullPoints = Assignment(id = 5L, availablePoints = null)
        section.assignments.add(assignmentWithNullPoints)

        val exception = assertThrows(IllegalStateException::class.java) {
            section.availablePoints
        }
        assertThat(exception.message).contains("Some assignments of this section have null availablePoints")
    }
}