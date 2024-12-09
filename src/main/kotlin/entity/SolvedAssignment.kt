package ut.isep.management.model.entity

import entity.BaseEntity
import jakarta.persistence.*
import java.io.Serializable
import java.util.*


    @Entity
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "solved_assignment_type", discriminatorType = DiscriminatorType.STRING)
    abstract class SolvedAssignment(
        @EmbeddedId
        override val id: SolvedAssignmentId = SolvedAssignmentId(),

        @ManyToOne(cascade = [CascadeType.PERSIST])
        @MapsId("inviteId") // Links inviteId to the Invite entity
        @JoinColumn(name = "invite_id")
        open var invite: Invite? = null,

        @OneToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
        @MapsId("assignmentId")
        @JoinColumn(name = "assignment_id")
        open val assignment: Assignment? = null
    ) : BaseEntity<SolvedAssignmentId>

    @Embeddable
    data class SolvedAssignmentId(
        val inviteId: UUID = UUID(0,0),
        val assignmentId: Long = 0
    ) : Serializable