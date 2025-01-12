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
    open val assignments: List<Assignment> = emptyList(),

    @Column(nullable = false)
    open val availablePoints: Int = assignments.sumOf { it.availablePoints!! },

    @Column(nullable = false)
    open val availableSeconds: Long = assignments.sumOf { it.availableSeconds!! }
) : BaseEntity<Long>
