package ut.isep.management.model.entity

import enumerable.ApplicantStatus
import jakarta.persistence.*

@Entity
open class Applicant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    open var name: String = "",
    open var email: String = "",
    open var status: ApplicantStatus = ApplicantStatus.not_started,
    open var score: Int? = null,
    open var preferredLanguage: String? = null,

    @OneToOne(mappedBy = "applicant", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var invite: Invite? = null
) : BaseEntity<Long>