package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
open class Section(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @Column(nullable = false)
    open var title: String? = null,

    @ManyToOne
    @JoinColumns(
        JoinColumn(name = "assessment_tag", referencedColumnName = "tag", nullable = false),
        JoinColumn(name = "assessment_gitCommitHash", referencedColumnName = "gitCommitHash", nullable = false)
    )
    open var assessment: Assessment? = null,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "section_assignments",
        joinColumns = [JoinColumn(name = "section_id")],
        inverseJoinColumns = [JoinColumn(name = "assignment_id")]
    )
    open val assignments: MutableList<Assignment> = mutableListOf()
) : BaseEntity<Long> {

    val availablePoints: Int
        get() = assignments.sumOf {
            it.availablePoints
                ?: throw IllegalStateException("Some assignments of this section have null availablePoints")
        }

    fun addAssignment(assignment: Assignment) {
        if (!assignments.contains(assignment)) {
            assignments.add(assignment)
        } else {
            throw IllegalArgumentException("Assignment with ID ${assignment.id} is already present in the section.")
        }
    }
    fun removeAssignmentById(assignmentId: Long) {
        val removed = assignments.removeIf { it.id == assignmentId }
        if (!removed) {
            throw IllegalArgumentException("Assignment with ID $assignmentId not found.")
        }
    }
}
