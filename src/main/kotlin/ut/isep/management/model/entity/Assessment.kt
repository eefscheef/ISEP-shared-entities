package ut.isep.management.model.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
open class Assessment(
    @EmbeddedId
    override val id: AssessmentID = AssessmentID(),

    @OneToMany(mappedBy = "assessment", cascade = [CascadeType.ALL])
    open val sections: MutableList<Section> = mutableListOf(),

    @OneToMany(mappedBy = "assessment", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val invites: MutableList<Invite> = mutableListOf(),

    @Column(nullable = false)
    open val availablePoints: Int = sections.sumOf {it.availablePoints}
): BaseEntity<AssessmentID>

@Embeddable
data class AssessmentID(
    @Column(nullable = false)
    val tag: String? = null,
    @Column(nullable = false)
    val gitCommitHash: String? = null,
) : Serializable