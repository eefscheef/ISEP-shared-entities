package ut.isep.management.model.entity

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AssignmentTypeTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun `test enum serialization`() {
        val json = objectMapper.writeValueAsString(AssignmentType.CODING)
        assertThat(json).isEqualTo("\"coding\"")
    }

    @Test
    fun `test enum deserialization`() {
        val assignmentType = objectMapper.readValue("\"multiple-choice\"", AssignmentType::class.java)
        assertThat(assignmentType).isEqualTo(AssignmentType.MULTIPLE_CHOICE)
    }
}