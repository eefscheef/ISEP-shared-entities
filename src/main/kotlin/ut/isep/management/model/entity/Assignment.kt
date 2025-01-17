package ut.isep.management.model.entity

import jakarta.persistence.*
import java.io.File
import java.util.*

@Entity
open class Assignment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0,
    @Column(nullable = false)
    val baseFilePath: String? = null,
    @Column(nullable = false)
    @Enumerated
    val assignmentType: AssignmentType? = null,
    @Column(nullable = false)
    val availablePoints: Int? = null,
) : BaseEntity<Long> {

    val sectionTitle: String
        get() = File(baseFilePath!!).parent
            ?: throw IllegalStateException("Can't find parent directory of Assignment $id with filePath $baseFilePath")

    val filePathWithId: String
        get() = if (id == 0L) throw IllegalStateException() else
            // TODO() replace with call to QuestionIDUtil.injectQuestionID
            baseFilePath!!.substringBeforeLast(".md") + "_qid$id.md"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Assignment) return false

        return id == other.id &&
                assignmentType == other.assignmentType &&
                baseFilePath == other.baseFilePath &&
                availablePoints == other.availablePoints
    }

    override fun hashCode(): Int {
        return listOf(id, baseFilePath, assignmentType, availablePoints).hashCode()
    }
}

enum class AssignmentType(val type: String) {
    CODING("coding"),
    MULTIPLE_CHOICE("multiple-choice"),
    OPEN("open");

    companion object {
        fun fromString(type: String): AssignmentType {
            return entries.find { it.type == type.lowercase(Locale.getDefault()) }
                ?: throw IllegalArgumentException("Unknown question type: $type")
        }
    }
}