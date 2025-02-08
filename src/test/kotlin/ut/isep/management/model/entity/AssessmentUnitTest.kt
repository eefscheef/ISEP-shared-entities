package ut.isep.management.model.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AssessmentUnitTest {

    @Test
    fun `test isLatest should return false when latest is null`() {
        val assessment = Assessment(latest = null)
        assertThat(assessment.isLatest).isFalse()
    }

    @Test
    fun `test isLatest should return true when latest is true`() {
        val assessment = Assessment(latest = true)
        assertThat(assessment.isLatest).isTrue()
    }

    @Test
    fun `test availableSeconds should sum section availableSeconds`() {
        val section1 = Section(assignments = mutableListOf(Assignment(availableSeconds = 120)))
        val section2 = Section(assignments = mutableListOf(Assignment(availableSeconds = 180)))

        val assessment = Assessment(sections = mutableListOf(section1, section2))

        assertThat(assessment.availableSeconds).isEqualTo(300)
    }

    @Test
    fun `test availablePoints should sum section availablePoints`() {
        val section1 = Section(assignments = mutableListOf(Assignment(availablePoints = 50)))
        val section2 = Section(assignments = mutableListOf(Assignment(availablePoints = 30)))

        val assessment = Assessment(sections = mutableListOf(section1, section2))

        assertThat(assessment.availablePoints).isEqualTo(80)
    }

    @Test
    fun `test addSection should add a section and set its assessment reference`() {
        val assessment = Assessment()
        val section = Section()

        assessment.addSection(section)

        assertThat(assessment.sections).contains(section)
        assertThat(section.assessment).isEqualTo(assessment)
    }

    @Test
    fun `test addSection should not add a section and set its assessment reference if it is already added`() {
        val section = Section()
        val assessment = Assessment(sections = mutableListOf(section))

        assessment.addSection(section)

        assertThat(assessment.sections).contains(section)
        assertThat(assessment.sections.size).isEqualTo(1)
    }

    @Test
    fun `test removeSection should remove a section and unset its assessment reference`() {
        val section = Section()
        val assessment = Assessment(sections = mutableListOf(section))
        section.assessment = assessment

        assessment.removeSection(section)

        assertThat(assessment.sections).doesNotContain(section)
        assertThat(section.assessment).isNull()
    }

    @Test
    fun `test removeSection should not remove a section that is not a section of that assessment`() {
        val section = Section()
        val removeSection = Section(assessment = Assessment())
        val assessment = Assessment(sections = mutableListOf(section))

        assessment.removeSection(removeSection)

        assertThat(assessment.sections).doesNotContain(removeSection)
        assertThat(assessment.sections).contains(section)
        assertThat(removeSection.assessment).isNotNull()
    }
}