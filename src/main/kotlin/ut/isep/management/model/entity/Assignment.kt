package ut.isep.management.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import parser.QuestionIDUtil
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
        get() = if (id == 0L || baseFilePath == null) throw IllegalStateException("Can't get filePathWithId for " +
                "likely detached entity with id: $id and baseFilePath $baseFilePath") else
            QuestionIDUtil.injectQuestionID(baseFilePath, id)

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

enum class AssignmentType {
    @JsonProperty("coding")
    CODING,
    @JsonProperty("multiple-choice")
    MULTIPLE_CHOICE,
    @JsonProperty("open")
    OPEN;
}