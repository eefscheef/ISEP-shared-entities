package parser.question

import ut.isep.management.model.entity.AssignmentType

data class CodingQuestion(
    override val id: Long?,
    override val tags: List<String>,
    override val description: String,
    override val filePath: String,
    override val availablePoints: Int,
    override val availableSeconds: Long,
    val code: CodingFile,
    val testCode: CodingFile,
    val secretTestCode: CodingFile,
) : Question {
    override val type: AssignmentType = AssignmentType.CODING
}

data class CodingFile(
    val filename: String,
    val code: String
)
