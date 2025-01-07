package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
open class Section(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @Column(nullable = false)
    open var title: String? = null,

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    open var assessment: Assessment? = null,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "section_assignments",
        joinColumns = [JoinColumn(name = "section_id")],
        inverseJoinColumns = [JoinColumn(name = "assignment_id")]
    )
    open val assignments: MutableList<Assignment> = mutableListOf(),

): BaseEntity<Long> {

    val availablePoints: Int
        get() = assignments.sumOf {
            it.availablePoints ?: throw IllegalStateException("Some assignments of this section have null availablePoints")
        }

    fun addAssignment(assignment: Assignment) {
        if (!assignments.contains(assignment)) {
            assignments.add(assignment)
            assignment.assessment.add(assessment ?: throw IllegalStateException("Cannot add assignments to Sections with no Assessment of this section have null availablePoints"))
        }
    }

    fun removeAssignment(assignment: Assignment) {
        if (assignments.contains(assignment)) {
            assignments.remove(assignment)
            assignment.assessment.remove(assessment)
        }
    }

}
