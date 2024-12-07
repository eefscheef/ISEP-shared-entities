package ut.isep.management.model.entity

import jakarta.persistence.*
import java.net.URI

@Entity
@DiscriminatorValue("CODING")
class AssignmentCoding(
    description: String = "",
    id: Long = 0,
    var codeUri: URI = URI(""),
    var language: String = "",
) : Assignment(id, description)
