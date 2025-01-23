package ut.isep.management.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import parser.QuestionIDUtil
import java.io.File

@Entity
open class Assignment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0,
    @Column(nullable = false)
    open val baseFilePath: String? = null,
    @Column(nullable = false)
    @Enumerated
    open val assignmentType: AssignmentType? = null,
    @Column(nullable = false)
    open val availablePoints: Int? = null,
) : BaseEntity<Long> {

    open val sectionTitle: String
        get() = File(baseFilePath!!).parent
            ?: throw IllegalStateException("Can't find parent directory of Assignment $id with filePath $baseFilePath")

    open val filePathWithId: String
        get() = if (id == 0L || baseFilePath == null) throw IllegalStateException("Can't get filePathWithId for " +
                "likely detached entity with id: $id and baseFilePath $baseFilePath") else
            QuestionIDUtil.injectQuestionID(baseFilePath!!, id)

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
    /**
     * @throws SecurityException
     */
    @PostPersist
    fun updateAssignmentFilename() {
        val existingFile = File(baseFilePath!!)
        val newFilename = filePathWithId
        existingFile.renameTo(File(newFilename))
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