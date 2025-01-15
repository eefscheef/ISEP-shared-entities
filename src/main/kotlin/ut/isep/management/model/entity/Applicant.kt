package ut.isep.management.model.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
open class Applicant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @Column(nullable = false)
    open val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(nullable = false)
    open var name: String? = null,
    @Column(nullable = false)
    open var email: String? = null,
    open var score: Int? = null,
    open var preferredLanguage: String? = null,

    @OneToMany(mappedBy = "applicant", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val invites: MutableList<Invite> = mutableListOf()
) : BaseEntity<Long>