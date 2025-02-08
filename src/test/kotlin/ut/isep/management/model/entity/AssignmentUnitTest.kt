package ut.isep.management.model.entity

import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import parser.QuestionIDUtil
import java.io.File

class AssignmentUnitTest {
    @Test
    fun `test sectionTitle returns correct parent directory name`() {
        val assignment = Assignment(baseFilePath = "/path/to/section/question.txt")
        assertThat(assignment.sectionTitle).isEqualTo("section")
    }

    @Test
    fun `test sectionTitle throws exception when parent directory is null`() {
        val assignment = Assignment(baseFilePath = "question.txt")
        val exception = assertThrows<IllegalStateException> { assignment.sectionTitle }
        assertThat(exception.message).contains("Can't find parent directory")
    }

    @Test
    fun `test filePathWithId returns correct path`() {
        mockkObject(QuestionIDUtil)
        every { QuestionIDUtil.injectQuestionID(any(), any()) } answers { "/path/to/section/question_123.txt" }

        val assignment = Assignment(id = 123, baseFilePath = "/path/to/section/question.txt")
        assertThat(assignment.filePathWithId).isEqualTo("/path/to/section/question_123.txt")

        unmockkObject(QuestionIDUtil)
    }

    @Test
    fun `test filePathWithId throws exception when id is 0`() {
        val assignment = Assignment(id = 0, baseFilePath = "/path/to/section/question.txt")
        val exception = assertThrows<IllegalStateException> { assignment.filePathWithId }
        assertThat(exception.message).contains("Can't get filePathWithId for likely detached entity")
    }


    @Test
    fun `test equals() should return true for same instance`() {
        val assignment = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        assertThat(assignment).isEqualTo(assignment)
    }

    @Test
    fun `test equals() should return true for two identical assignments`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)

        assertThat(assignment1).isEqualTo(assignment2)
        assertThat(assignment1.hashCode()).isEqualTo(assignment2.hashCode())
    }

    @Test
    fun `test equals() should return false for different assignmentType`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.OPEN, baseFilePath = "/path/file1", availablePoints = 10)

        assertThat(assignment1).isNotEqualTo(assignment2)
    }

    @Test
    fun `test equals() should return false for different baseFilePath`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file2", availablePoints = 10)

        assertThat(assignment1).isNotEqualTo(assignment2)
    }

    @Test
    fun `test equals() should return false for different availablePoints`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 20)

        assertThat(assignment1).isNotEqualTo(assignment2)
    }

    @Test
    fun `test equals() should return false for different ID`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 2L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)

        assertThat(assignment1).isNotEqualTo(assignment2)
    }

    @Test
    fun `test equals() should return false when compared with null`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)

        assertThat(assignment1).isNotEqualTo(null)
    }

    @Test
    fun `test equals() should return false when compared with different class`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val differentObject = "SomeString"

        assertThat(assignment1).isNotEqualTo(differentObject)
    }


    @Test
    fun `test hashCode() should be consistent for the same object`() {
        val assignment = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)

        val hashCode1 = assignment.hashCode()
        val hashCode2 = assignment.hashCode()

        assertThat(hashCode1).isEqualTo(hashCode2)
    }

    @Test
    fun `test hashCode() should be the same for two identical objects`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)

        assertThat(assignment1.hashCode()).isEqualTo(assignment2.hashCode())
    }

    @Test
    fun `test hashCode() should be different for different ids`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 2L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)

        assertThat(assignment1.hashCode()).isNotEqualTo(assignment2.hashCode())
    }

    @Test
    fun `test hashCode() should be different for different assignment types`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.OPEN, baseFilePath = "/path/file1", availablePoints = 10)

        assertThat(assignment1.hashCode()).isNotEqualTo(assignment2.hashCode())
    }

    @Test
    fun `test hashCode() should be different for different file paths`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file2", availablePoints = 10)

        assertThat(assignment1.hashCode()).isNotEqualTo(assignment2.hashCode())
    }

    @Test
    fun `test hashCode() should be different for different availablePoints`() {
        val assignment1 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 10)
        val assignment2 = Assignment(id = 1L, assignmentType = AssignmentType.CODING, baseFilePath = "/path/file1", availablePoints = 20)

        assertThat(assignment1.hashCode()).isNotEqualTo(assignment2.hashCode())
    }


    @Test
    @Disabled("Can't mock the File objects in the function")
    fun `test updateAssignmentFilename renames file when file exists`() {
        val existingFile = mockk<File>()
        mockkStatic(File::renameTo)
        every { existingFile.exists() } returns true
        every { existingFile.renameTo(any()) } returns true

        mockkObject(QuestionIDUtil)
        every { QuestionIDUtil.injectQuestionID(any(), any()) } returns "/test/question_123.txt"

        val assignment = Assignment(id = 123, baseFilePath = "/test/question.txt")

        assignment.updateAssignmentFilename()

        assertTrue(existingFile.exists())
        verify { existingFile.renameTo(File("/test/question_123.txt")) }

        unmockkAll()
    }

    @Test
    fun `test updateAssignmentFilename prints warning when file does not exist`() {
        mockkStatic(File::class)
        val nonExistingFile = mockk<File>(relaxed = true)
        every { nonExistingFile.exists() } returns false

        val assignment = Assignment(id = 123, baseFilePath = "/path/to/section/question.txt")

        assignment.updateAssignmentFilename() // Should print warning but not crash

        verify(exactly = 0) { nonExistingFile.renameTo(any()) }

        unmockkAll()
    }
}