package ut.isep.management.model.entity

import entity.BaseEntity
import jakarta.persistence.*

@Entity
open class Assessment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @OneToMany(mappedBy = "assessment", cascade = [CascadeType.ALL])
    open val sections: MutableList<Section> = mutableListOf(),

    @OneToMany(mappedBy = "assessment", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val invites: MutableList<Invite> = mutableListOf()
): BaseEntity<Long>