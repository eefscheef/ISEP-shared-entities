package ut.isep.management.model.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class TimingPerSection(
    @EmbeddedId
    override val id: TimingPerSectionId = TimingPerSectionId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inviteId") // Links inviteId to the Invite entity
    @JoinColumn(name = "invite_id")
    open var invite: Invite? = null,

    @OneToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
    @MapsId("sectionId")
    @JoinColumn(name = "section_id", nullable = false)
    open val section: Section? = null,

    open var seconds: Long = 0,

    open var visitedAt: OffsetDateTime? = null,

    ) : BaseEntity<TimingPerSectionId>

@Embeddable
data class TimingPerSectionId(
    val inviteId: UUID = UUID(0, 0),
    val sectionId: Long = 0
) : Serializable