package ut.isep.management.model.entity

import jakarta.persistence.*

@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["tag", "gitCommitHash"]),
        UniqueConstraint(columnNames = ["tag", "latest"]),
    ]
)
@Entity
open class Assessment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,

    @Column(nullable = false)
    open val tag: String? = null,

    open var gitCommitHash: String? = null,

    @OneToMany(mappedBy = "assessment", cascade = [CascadeType.ALL])
    open val sections: MutableList<Section> = mutableListOf(),

    @OneToMany(mappedBy = "assessment", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val invites: MutableList<Invite> = mutableListOf(),

    @Column(nullable = true)
    open var latest: Boolean? = null

) : BaseEntity<Long> {

    val isLatest: Boolean
        get() = latest ?: false

    val availableSeconds: Long
        get() = sections.sumOf { it.availableSeconds }

    val availablePoints: Int
        get() = sections.sumOf { it.availablePoints }

    fun addSection(section: Section) {
        if (!sections.contains(section)) {
            sections.add(section)
            section.assessment = this
        }
    }

    fun removeSection(section: Section) {
        if (sections.contains(section)) {
            sections.remove(section)
            section.assessment = null
        }
    }
}
