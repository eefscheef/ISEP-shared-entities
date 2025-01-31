package parser.question

import ut.isep.management.model.entity.AssignmentType

data class CodingQuestion(
    override val id: Long?,
    override val tags: List<String>,
    override val description: String,
    override val filePath: String,
    override val availablePoints: Int,
    override val availableSeconds: Long,
    val language: String,
    val files: CodeQuestionFiles
) : Question {
    override val type: AssignmentType = AssignmentType.CODING
}

data class CodeQuestionFiles(
    val code: CodingFile,
    val test: CodingFile,
    val secretTest: CodingFile,
    val referenceCode: CodingFile? = null,
    val referenceTest: CodingFile? = null
)

data class CodingFile(
    val filename: String,
    val content: String
)
