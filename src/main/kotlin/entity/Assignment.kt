package ut.isep.management.model.entity

import entity.BaseEntity
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "assignment_type", discriminatorType = DiscriminatorType.STRING)
abstract class Assignment protected constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    open var description: String = "",
) : BaseEntity<Long>