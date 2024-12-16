package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
open class Applicant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    open var name: String = "",
    open var email: String = "",
    open var score: Int? = null,
    open var preferredLanguage: String? = null,

    @OneToMany(mappedBy = "applicant", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val invites: MutableList<Invite> = mutableListOf()
) : BaseEntity<Long>