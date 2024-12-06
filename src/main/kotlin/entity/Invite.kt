package ut.isep.management.model.entity

import entity.BaseEntity
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity
class Invite(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override val id: UUID = UUID.randomUUID(),

    @OneToOne
    @JoinColumn(name = "applicant_id")
    val applicant: Applicant = Applicant(),

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    val assessment: Assessment = Assessment(),

    val invitedAt: ZonedDateTime = ZonedDateTime.now()
) : BaseEntity<UUID>
