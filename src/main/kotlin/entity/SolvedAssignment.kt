package ut.isep.management.model.entity

import entity.BaseEntity
import jakarta.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "solved_assignment_type", discriminatorType = DiscriminatorType.STRING)
abstract class SolvedAssignment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    open var assignment: Assignment? = null,

    @ManyToOne
    @JoinColumn(name = "invite_id")
    open var invite: Invite? = null
) : BaseEntity<Long>
