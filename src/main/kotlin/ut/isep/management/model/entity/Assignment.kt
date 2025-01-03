package ut.isep.management.model.entity

import jakarta.persistence.*
import java.util.*

@Entity
open class Assignment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @Column(nullable = false)
    val filePath: String? = null,
    @Column(nullable= false)
    @Enumerated
    val assignmentType: AssignmentType? = null,
    @Column(nullable = false)
    val availablePoints: Int? = null
) : BaseEntity<Long>

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