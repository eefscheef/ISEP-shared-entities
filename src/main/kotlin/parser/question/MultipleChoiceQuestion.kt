package parser.question

import ut.isep.management.model.entity.AssignmentType

data class MultipleChoiceQuestion(
    override val id: Long?,
    override val filePath: String,
    override val tags: List<String>,
    override val description: String,
    val options: List<Option>
) : Question {
    override val type: AssignmentType = AssignmentType.MULTIPLE_CHOICE

    data class Option(
        val text: String,
        val isCorrect: Boolean
    )
}
