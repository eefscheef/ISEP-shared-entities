package ut.isep.management.model.entity

import entity.BaseEntity
import jakarta.persistence.*

@Entity
open class Section(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    open var title: String = "",

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    open var assessment: Assessment? = null,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "section_assignments",
        joinColumns = [JoinColumn(name = "section_id")],
        inverseJoinColumns = [JoinColumn(name = "assignment_id")]
    )
    open val assignments: List<Assignment> = emptyList()
): BaseEntity<Long>
