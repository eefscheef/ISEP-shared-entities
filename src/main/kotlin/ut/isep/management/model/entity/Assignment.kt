package ut.isep.management.model.entity

import jakarta.persistence.*
import java.util.*

@Entity
open class Assignment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0,
    @Column(nullable = false)
    val filePath: String? = null,
    @Column(nullable = false)
    @Enumerated
    val assignmentType: AssignmentType? = null,
    @Column(nullable = false)
    val availablePoints: Int? = null,

    @ManyToMany
    @JoinColumn(name = "section_id", nullable = false)
    open val sections: MutableList<Section> = mutableListOf()
) : BaseEntity<Long> {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Assignment) return false

        return id == other.id &&
                assignmentType == other.assignmentType &&
                filePath == other.filePath &&
                availablePoints == other.availablePoints
    }

    override fun hashCode(): Int {
        return listOf(id, filePath, assignmentType, availablePoints).hashCode()
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