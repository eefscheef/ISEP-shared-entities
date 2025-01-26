package ut.isep.management.model.entity

import enumerable.UserRole
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Entity
@Table(name = "AppUser")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @Column(nullable = false, unique = true)
    open var oid: String? = null, // comes from Microsoft Azure
    @Column(nullable = false)
    open val createdAt: OffsetDateTime = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC),
    @Column(nullable = false)
    open var name: String? = null,
    @Column(nullable = false)
    open var email: String? = null,
    open var role: UserRole? = null
) : BaseEntity<Long>