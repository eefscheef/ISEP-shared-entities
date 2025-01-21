package parser.question

import ut.isep.management.model.entity.Assignment
import ut.isep.management.model.entity.AssignmentType

sealed interface Question {
    val id: Long? // Unique identifier for the question, can be null for new questions
    val filePath: String
    val type: AssignmentType
    val tags: List<String>
    val description: String

    fun toEntity(): Assignment {
        return id?.let {id ->
            Assignment(id = id, baseFilePath = filePath, assignmentType = type)
        } ?: Assignment(baseFilePath = filePath, assignmentType = type)
    }
}
