package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "assignment_type", discriminatorType = DiscriminatorType.STRING)
abstract class Assignment protected constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @Column(nullable = false)
    open var description: String? = null,
    @Column(nullable = false)
    open var availablePoints: Int? = null,
    @Column(nullable = false)
    open var availableSeconds: Long? = null,
) : BaseEntity<Long>