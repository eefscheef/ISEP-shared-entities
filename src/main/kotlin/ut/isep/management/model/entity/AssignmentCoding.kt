package ut.isep.management.model.entity

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.net.URI

@Entity
@DiscriminatorValue("CODING")
class AssignmentCoding(
    description: String? = null,
    id: Long = 0,
    availablePoints: Int? = null,
    availableSeconds: Long? = null,

    @Column(nullable = true)
    var codeUri: URI? = null,

    @Column(nullable = true)
    var language: String? = null,

    @Column(nullable = true)
    var referenceAnswer: String? = null,
) : Assignment(id, description, availablePoints, availableSeconds)
