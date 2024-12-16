package ut.isep.management.model.entity

import jakarta.persistence.*
import java.net.URI

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
class GitHubAssignmentMultipleChoice(
    @Id
    override val id: Long = 0,
    val url: URI = URI(""),
) : BaseEntity<Long>