package ut.isep.management.model.entity

import enumerable.UserRole
import jakarta.persistence.*

@Entity
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0,
    open var name: String = "",
    open var email: String = "",
    open var role: UserRole? = null
)