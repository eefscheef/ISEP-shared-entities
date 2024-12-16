package ut.isep.management.model.entity

import jakarta.persistence.*
import java.net.URI

@Entity
@DiscriminatorValue("CODING")
class AssignmentCoding(
    description: String? = null,
    id: Long = 0,

    @Column(nullable = false)
    var codeUri: URI? = null,

    @Column(nullable = false)
    var language: String? = null,

    @Column(nullable = false)
    var referenceAnswer: String? = null,
) : Assignment(id, description)
