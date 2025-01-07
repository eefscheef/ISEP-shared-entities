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

    open var latest: Boolean = false

): BaseEntity<AssessmentID> {
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

@Embeddable
data class AssessmentID(
    @Column(nullable = false)
    val tag: String? = null,
    @Column(nullable = false)
    val gitCommitHash: String? = null,
) : Serializable