package ut.isep.management.model.entity

import enumerable.UserRole
import jakarta.persistence.*

@Entity
@Table(name = "AppUser")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    open var name: String = "",
    open var email: String = "",
    open var role: UserRole? = null
) : BaseEntity<Long>